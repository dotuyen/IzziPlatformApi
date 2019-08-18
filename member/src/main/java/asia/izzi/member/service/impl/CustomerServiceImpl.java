package asia.izzi.member.service.impl;

import asia.izzi.member.common.AppConstant;
import asia.izzi.member.domain.dto.CustomerImport;
import asia.izzi.member.domain.dto.CustomerImportResponse;
import asia.izzi.member.domain.model.Category;
import asia.izzi.member.domain.model.ContentLanguage;
import asia.izzi.member.domain.model.Customer;
import asia.izzi.member.domain.model.UserCategoryRelation;
import asia.izzi.member.repository.CategoryRepository;
import asia.izzi.member.repository.ContentLanguageRepository;
import asia.izzi.member.repository.CustomerRepository;
import asia.izzi.member.repository.UserCategoryRelationRepository;
import asia.izzi.member.service.AuthenticationService;
import asia.izzi.member.service.CustomerService;
import asia.izzi.member.util.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public List<Customer> find(String textSearch1, String textSearch2, String textSearch3) {
        return null;
    }

    @Override
    public List<Customer> findAllByPrice(double price, Pageable pageable) {
        return null;
    }

    @Autowired
    private Environment env;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ContentLanguageRepository contentLanguageRepository;

    @Autowired
    private UserCategoryRelationRepository userCategoryRelationRepository;

    @Autowired
    private AuthenticationService authenticationService;


    final String CUSTOMER_EXPORT = "customers";


    @Override
    public List<Map> getCategoryInTypeCustomer(String textSearch) {
        List<Object[]> listObject = categoryRepository.getCategoryInTypeCustomer(textSearch);
        List<Map> listCategory = new ArrayList<>();
        if (listObject != null) {
            listObject.stream().forEach(object -> {
                Map map = new HashMap();
                map.put("id", object[0]);
                map.put("typeName", object[1]);
                listCategory.add(map);
            });
        }
        return listCategory;
    }


    @Override
    public CustomerImportResponse getDataImportExcel(InputStream inputStream) throws Exception {
        int START_ROW_READ = 3;
        int COL_NAME = 1;
        int COL_PHONE = 2;
        int COL_EMAIL = 3;
        int COL_TYPE = 4;
        // read data to excel file
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        List<Map<Integer, String>> listImport = ExcelUtils.getDataExcel(START_ROW_READ, workbook);

        //import response
        CustomerImportResponse customerImportResponse = new CustomerImportResponse();
        List<CustomerImport> listCustomerError = new ArrayList<>();
        List<CustomerImport> listCustomerSuccess = new ArrayList<>();
        boolean isErrorImport = false;
        if (listImport != null) {
            List<String> listEmail = new ArrayList<>();
            List<String> listPhone = new ArrayList<>();
            List<CustomerImport> listCustomerValidate = new ArrayList<>();
            Map<String, Integer> mapEmail = new HashMap<>();
            Map<String, Integer> mapPhone = new HashMap<>();
            for (Map<Integer, String> mapImport : listImport) {
                CustomerImport customer = new CustomerImport();
                customer.setName(mapImport.get(COL_NAME));
                customer.setPhone(mapImport.get(COL_PHONE));
                customer.setEmail(mapImport.get(COL_EMAIL));
                customer.setType(mapImport.get(COL_TYPE));

                // validate data
                StringBuilder error = new StringBuilder();
                error.append(validateCustomerImport(customer));

                if (mapEmail.get(customer.getEmail()) == null) {
                    mapEmail.put(customer.getEmail(), 1);
                } else {
                    mapEmail.put(customer.getEmail(), mapEmail.get(customer.getEmail()) + 1);
                }

                if (mapPhone.get(customer.getPhone()) == null) {
                    mapPhone.put(customer.getPhone(), 1);
                } else {
                    mapPhone.put(customer.getPhone(), mapEmail.get(customer.getEmail()) + 1);
                }

                if (error.length() == 0) {
                    // check duplicate
                    if (!listEmail.contains(customer.getEmail())) {
                        listEmail.add(customer.getEmail());
                    }
                    if (mapEmail.get(customer.getEmail()) > 1) {
                        error.append(MessageUtils.getMessage("email.is-duplicate") + "; ");
                    }

                    if (!listPhone.contains(customer.getPhone())) {
                        listPhone.add(customer.getPhone());
                    }
                    if (mapPhone.get(customer.getPhone()) > 1) {
                        error.append(MessageUtils.getMessage("phone.is-duplicate") + "; ");
                    }
                }

                customer.setError(error.toString());
                if (error.length() == 0) {
                    listCustomerValidate.add(customer);
                } else {
                    listCustomerError.add(customer);
                    isErrorImport = true;
                }
            }

            // check email, phone  exist database
            List<Customer> listCustomerByEmail = customerRepository.findByEmailIn(listEmail);
            List<Customer> listCustomerByPhone = customerRepository.findByMobileIn(listPhone);
            List<String> listEmailDup = new ArrayList<>();
            List<String> listPhoneDup = new ArrayList<>();
            for (Customer customer : listCustomerByEmail) {
                listEmailDup.add(customer.getEmail());
            }
            for (Customer customer : listCustomerByPhone) {
                listPhoneDup.add(customer.getMobile());
            }

            for (CustomerImport customer : listCustomerValidate) {
                StringBuilder error = new StringBuilder();
                if (listEmailDup.contains(customer.getEmail())) {
                    error.append(MessageUtils.getMessage("email.is-exist") + "; ");
                }

                if (listPhoneDup.contains(customer.getPhone())) {
                    error.append(MessageUtils.getMessage("phone.is-exist") + "; ");
                }


                if (error.length() == 0) {
                    listCustomerSuccess.add(customer);
                } else {
                    customer.setError(error.toString());
                    listCustomerError.add(customer);
                    isErrorImport = true;
                }
            }

            customerImportResponse.setSuccess(listCustomerSuccess.size());
            customerImportResponse.setError(listCustomerError.size());
            customerImportResponse.setTotal(listCustomerSuccess.size() + listCustomerError.size());
            customerImportResponse.setListErrorImport(listCustomerError);

            // import data success to database
            // todo: thieu xu ly Category (type) thong phan nhom cua nguoi dung

            saveCustomer(listCustomerSuccess);

            // if error then write excel error
            if (isErrorImport) {
                customerImportResponse.setFileName(writeExcel(listCustomerError));
            }
        }

        return customerImportResponse;
    }


    public String validateCustomerImport(CustomerImport customer) {
        StringBuilder error = new StringBuilder();

        if (customer.getName() == null || customer.getName().isEmpty()) {
            error.append(MessageUtils.getMessage("customer.name.is-required") + "; ");
        }

        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            error.append(MessageUtils.getMessage("email.is-required") + "; ");
        } else if (!EmailUtils.isEmail(customer.getEmail())) {
            error.append(MessageUtils.getMessage("email.is-valid") + "; ");
        }

        if (customer.getPhone() == null || customer.getPhone().isEmpty()) {
            error.append(MessageUtils.getMessage("phone.is-required") + "; ");
        } else if (!PhoneUtils.isPhone(customer.getPhone())) {
            error.append(MessageUtils.getMessage("phone.is-valid") + "; ");
        }
        return error.toString();
    }


    @Transactional
    public void saveCustomer(List<CustomerImport> listCustomerSuccess) {
        try {
            if (!(listCustomerSuccess == null || listCustomerSuccess.isEmpty())) {
                List<Object[]> listObject = categoryRepository.getCategoryNameOfCustomer();
                List<String> listCategoryContent = new ArrayList<>();
                if (listObject != null) {
                    listObject.stream().forEach(object -> {
                        String categoryContent = (String) object[0];
                        listCategoryContent.add(categoryContent);
                    });
                }
                List<Map<String, String[]>> listMapContentLanguage = new ArrayList<>();
                boolean isExistCategory = false;
                // customer
                for (CustomerImport cus : listCustomerSuccess) {
                    Customer customer = Customer.builder()
                            .userName(cus.getName())
                            .email(cus.getEmail())
                            .mobile(PhoneUtils.formatPhoneNumber(cus.getPhone()))
                            .merchantId(authenticationService.getCurrentUser().getMerchantId())
                            .deleted(AppConstant.CUSTOMER.DEFAULT_DELETED)
                            .version(AppConstant.CUSTOMER.DEFAULT_VERSION)
                            .build();
                    customerRepository.save(customer);

                    Map<String, String[]> mapContentLanguage = new HashMap<>();
                    String[] categoryNames = new String[]{};
                    if (cus.getType() != null) {
                        categoryNames = cus.getType().split(",");
                        cus.setCategoryNames(categoryNames);
                    }

                    for (String content : categoryNames) {
                        if (listCategoryContent.contains(content)) {
                            isExistCategory = true;
                        }
                    }
                    if (!isExistCategory) {
                        mapContentLanguage.put(customer.getId(), categoryNames);
                        listMapContentLanguage.add(mapContentLanguage);

                    }

                }

                for (Map<String, String[]> item : listMapContentLanguage) {
                    for (Map.Entry<String, String[]> entry : item.entrySet()) {
                        for (String content : entry.getValue()) {
                            //category
                            Category category = Category.builder()
                                    .type(AppConstant.CATEGORY.TYPE_CUSTOMER)
                                    .deleted(AppConstant.CATEGORY.DEFAULT_DELETED)
                                    .published(AppConstant.CATEGORY.DEFAULT_PUBLISHED)
                                    .version(AppConstant.CATEGORY.DEFAULT_VERSION)
                                    .parentId("")
                                    .merchantId(authenticationService.getCurrentUser().getMerchantId())
                                    .build();
                            categoryRepository.save(category);
                            // ContentLanguage
                            ContentLanguage contentLanguage = ContentLanguage.builder()
                                    .entityId(category.getId())
                                    .field(AppConstant.CONTENT_LANGUAGE.TYPE_FIELD)
                                    .languageId(UUID.randomUUID().toString())
                                    .content(content)
                                    .build();
                            contentLanguageRepository.save(contentLanguage);
                            // userCategoryRelation
                            UserCategoryRelation userCategoryRelation = UserCategoryRelation.builder()
                                    .categoryId(category.getId())
                                    .customerId(entry.getKey())
                                    .merchantId(authenticationService.getCurrentUser().getMerchantId())
                                    .build();
                            userCategoryRelationRepository.save(userCategoryRelation);

                        }

                    }
                }
            }
        } catch (Exception ex) {

        }
    }

    public String writeExcel(List<CustomerImport> listCustomer) throws Exception {
        Context context = new Context();
        context.putVar(CUSTOMER_EXPORT, listCustomer);
        InputStream fileInputStream = new ClassPathResource("templates/FormCustomerExport.xlsx").getInputStream();
        String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String destFileName = env.getProperty("spring.app.path.upload") + File.separator + timeNow;
        File dirDest = new File(destFileName);
        if (!dirDest.exists()) {
            dirDest.mkdirs();
        }

        // todo: ma hoa ten file
        String fileImportResultName = timeNow + File.separator + "CustomerImportResult.xlsx";
        OutputStream fileOutputStream = new FileOutputStream(env.getProperty("spring.app.path.upload") + File.separator + fileImportResultName);
        ExcelUtils.exportExcel(fileInputStream, context, fileOutputStream);
        return EncryptUtils.encryptFileUploadPath(fileImportResultName);

    }


}

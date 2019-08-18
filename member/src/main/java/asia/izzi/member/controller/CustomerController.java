package asia.izzi.member.controller;

import asia.izzi.member.domain.dto.CustomerImportResponse;
import asia.izzi.member.service.CustomerService;
import asia.izzi.member.util.EncryptUtils;
import asia.izzi.member.util.MediaTypeUtils;
import asia.izzi.member.util.MessageUtils;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:9000")
@RequestMapping("/v1")
public class CustomerController {

    private final Logger log = LoggerFactory.getLogger(CustomerController.class);

    // todo: hunglq tach 2 controller 1 controller TemplateFileController , 1 Controller CustomerController
    @Autowired
    private ServletContext servletContext;

    @Autowired
    private Environment env;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customer/category-name")
    public ResponseEntity getCategoryInTypeCustomer(@RequestParam(value = "textSearch", required = false) String textSearch) {
        String textSearchFormat = textSearch == null ? "" : textSearch.trim();
        return new ResponseEntity<>(customerService.getCategoryInTypeCustomer(textSearchFormat), HttpStatus.OK);
    }

    @GetMapping("/customer/template-import")
    // todo: /template?type=customer , customer/template-import
    public ResponseEntity downloadFileTemp(HttpServletResponse response) throws IOException {
        InputStream fileInputStream = new ClassPathResource("templates/FormCustomer.xlsx").getInputStream();
        File fileDownload = File.createTempFile("test", ".xlsx");
        try (InputStream inputStream = new FileInputStream(fileDownload); OutputStream output = response.getOutputStream()) {
            FileUtils.copyInputStreamToFile(fileInputStream, fileDownload);
//            response.reset();
            response.setContentType("application/octet-stream");
            response.setContentLength((int) fileDownload.length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + "FormCustomer.xlsx" + "\"");
            IOUtils.copyLarge(inputStream, output);
            output.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/customer/import-result/{fileName}")
    // todo: /customer/import-result/{uuid}
    // todo:  -> return file
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("fileName") String fileName) throws Exception {
        String fileNameDeCrypt = EncryptUtils.decryptFileUploadPath(fileName);
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileNameDeCrypt);
        // todo cau hinh file trong resource, ko harcode duong dan
        String downloadPath = env.getProperty("spring.app.path.upload");
        File file = new File(downloadPath + File.separatorChar + fileNameDeCrypt);
        if (!file.exists()) {
            throw new IllegalArgumentException(MessageUtils.getMessage("file.is-not-found"));
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                // Content-Type
                .contentType(mediaType)
                .contentLength(file.length())
                .body(resource);
    }

    @PostMapping("/customer/import")
    // todo: /customer/import, return về kết quả + uuid,
    // todo : return ResponseEntity<Map>
    // todo : validate lỗi -> 400
    public ResponseEntity uploadFileExcel(@RequestParam("file") MultipartFile file) {
        CustomerImportResponse customerImportResponse = new CustomerImportResponse();
        try {

            // todo: sample validate
            Assert.notNull(file, MessageUtils.getMessage("file.is-not-null"));
            // todo xu ly du lieu : return 200, dto: total, susscee, error, list<customerDTO>

            //read file excel
            customerImportResponse = customerService.getDataImportExcel(file.getInputStream());

        } catch (Exception e) {
        }
        return new ResponseEntity<>(customerImportResponse, HttpStatus.OK);
    }

    @Autowired
    CustomerService userService;

    @GetMapping("/customer")
    // todo:  @RequestParam("type") chuyen thanh category
    public ResponseEntity getUsers(@RequestParam("textSearch") String textSearch, @RequestParam("type") List<String> type, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        //userService.getUsers(textSearch,type,page,pageSize);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity getUserById(@PathVariable("id") String id) {
        //userService.getUserById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/customer/{id}")
    public ResponseEntity updateUserById(@PathVariable("id") String id) {
        //userService.updateUserById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity deleteUserById(@PathVariable("id") String id) {
        //userService.deleteUserById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}

package asia.izzi.member.service;

import asia.izzi.member.domain.dto.CustomerImportResponse;
import asia.izzi.member.domain.model.Customer;
import org.springframework.data.domain.Pageable;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface CustomerService {

    List<Customer> find(String textSearch1, String textSearch2, String textSearch3);

    // Phân Trang
    List<Customer> findAllByPrice(double price, Pageable pageable);

    CustomerImportResponse getDataImportExcel(InputStream inputStream) throws Exception;

    List<Map> getCategoryInTypeCustomer(String textSearch);
}

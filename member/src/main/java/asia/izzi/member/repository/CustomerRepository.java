package asia.izzi.member.repository;

import asia.izzi.member.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Customer findByUserName(String username);

    List<Customer> findByEmail(String email);

    List<Customer> findByMobile(String phone);

    List<Customer> findByEmailIn(List<String> emails);

    List<Customer> findByMobileIn(List<String> mobiles);
}

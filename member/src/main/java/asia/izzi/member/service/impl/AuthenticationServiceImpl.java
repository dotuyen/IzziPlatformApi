package asia.izzi.member.service.impl;

import asia.izzi.member.domain.model.Customer;
import asia.izzi.member.service.AuthenticationService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    public Customer getCurrentUser() {
        // todo integtate with SecurityContext
        return Customer.builder().userName("system").merchantId("275ac045-2ae5-418a-87d7-85d967a37cb7").build();
    }

}

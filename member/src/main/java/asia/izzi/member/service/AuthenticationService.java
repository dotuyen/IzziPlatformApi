package asia.izzi.member.service;

import asia.izzi.member.domain.model.Customer;

public interface AuthenticationService {
    Customer getCurrentUser();
}

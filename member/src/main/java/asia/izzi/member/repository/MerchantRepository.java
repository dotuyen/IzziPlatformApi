package asia.izzi.member.repository;

import asia.izzi.member.domain.model.Merchant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends CrudRepository<Merchant, String> {
}

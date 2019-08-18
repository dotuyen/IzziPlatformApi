package asia.izzi.member.repository;

import asia.izzi.member.domain.model.UserCategoryRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCategoryRelationRepository extends JpaRepository<UserCategoryRelation, String> {
}

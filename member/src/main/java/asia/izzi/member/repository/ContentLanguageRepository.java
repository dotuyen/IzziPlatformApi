package asia.izzi.member.repository;

import asia.izzi.member.domain.model.ContentLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentLanguageRepository extends JpaRepository<ContentLanguage, String> {
}

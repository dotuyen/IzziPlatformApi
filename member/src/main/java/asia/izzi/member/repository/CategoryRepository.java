package asia.izzi.member.repository;

import asia.izzi.member.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query(value = "SELECT b.Content FROM category a INNER JOIN contentlanguage b ON a.Id = b.EntityId AND b.Field = 'Name' AND a.Type = 10", nativeQuery = true)
    List<Object[]> getCategoryNameOfCustomer();

    @Query(value = "SELECT a.Id, b.Content FROM category a INNER JOIN contentlanguage b ON a.Id = b.EntityId AND b.Field = 'Name' AND a.Type = 10 AND b.Content like %?1%", nativeQuery = true)
    List<Object[]> getCategoryInTypeCustomer(String textSearch);
}

package asia.izzi.member.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "usercategoryrelation")
@Data // lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCategoryRelation extends Auditable {
    @Id
    @Column(name = "Id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "customerId")
    private String customerId;

    @Column(name = "categoryId")
    private String categoryId;

    @Column(name = "merchantId")
    private String merchantId;

}

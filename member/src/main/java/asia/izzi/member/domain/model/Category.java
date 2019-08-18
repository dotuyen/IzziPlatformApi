package asia.izzi.member.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Data // lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends Auditable {
    @Id
    @Column(name = "Id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "ParentId")
    private String parentId;

    @Column(name = "Type")
    private int type;

    @Column(name = "Deleted")
    private int deleted;

    @Column(name = "Version")
    private Integer version;

    @Column(name = "Published")
    private Integer published;

    @Column(name = "MerchantId")
    private String merchantId;

}

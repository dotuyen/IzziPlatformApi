package asia.izzi.member.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "customer")
@Data // lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends Auditable {
    @Id
    @Column(name = "Id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Password")
    private String password;

    @Column(name = "Email")
    private String email;

    @Column(name = "Mobile")
    private String mobile;

    @Column(name = "Deleted")
    private int deleted;

    @Column(name = "Active")
    private int active;

    @Column(name = "UserName")
    private String userName;

    @Column(name = "Version")
    private int version;

    @Column(name = "MerchantId")
    private String merchantId;

}

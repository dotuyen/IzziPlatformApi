package asia.izzi.member.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "merchant")
@Data // lombok
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Merchant extends Auditable {
    @Id
    @Column(name = "Id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "Name")
    private String name;
    @Column(name = "Code")
    private String code;
    @Column(name = "Email")
    private String email;
    @Column(name = "Mobile")
    private String mobile;
    @Column(name = "Deleted")
    private int deleted;
    @Column(name = "Active")
    private int active;


}
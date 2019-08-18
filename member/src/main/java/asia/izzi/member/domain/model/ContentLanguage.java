package asia.izzi.member.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "contentlanguage")
@Data // lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentLanguage {
    @Id
    @Column(name = "Id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "EntityId")
    private String entityId;

    @Column(name = "LanguageId")
    private String languageId;

    @Column(name = "Field")
    private String field;

    @Column(name = "Content")
    private String content;
}

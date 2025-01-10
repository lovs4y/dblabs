package vladyslava.prazhmovska.dbrgr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import java.util.List;

@Data
@Table(name = "cvs")
@Entity
public class CV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String midName;
    private String description;
    private String recruiterComment;
    @Column(name = "is_hired")
    private Boolean hired;
    private Long recruiterId;
    private Long specialtyId;
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Skill> skills;
}

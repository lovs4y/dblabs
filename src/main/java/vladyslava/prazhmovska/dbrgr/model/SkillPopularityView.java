package vladyslava.prazhmovska.dbrgr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "skill_popularity_view")
public class SkillPopularityView {
    @Id
    private Long id;
    private String code;
    private BigDecimal averageKnowledgeRate;
    private Long popularityRate;
}

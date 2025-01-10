package vladyslava.prazhmovska.dbrgr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "recruiter_efficiency_view")
public class RecruiterEfficiencyView {
    @Id
    private Long id;
    private String email;
    private Long numberOfHired;
    private String mainRecruiterSpecialty;
    private BigDecimal averageKnowledgeRate;
    private Long totalProcessedCvs;
}

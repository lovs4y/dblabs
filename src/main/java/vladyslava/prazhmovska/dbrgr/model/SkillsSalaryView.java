package vladyslava.prazhmovska.dbrgr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "skills_salary_view")
public class SkillsSalaryView {
    @Id
    private Long id;
    private String name;
    private BigDecimal avgSkillSalary;
    private Long maxSkillSalary;
    private Long hiredApplicants;
    private String specialtyCode;
}

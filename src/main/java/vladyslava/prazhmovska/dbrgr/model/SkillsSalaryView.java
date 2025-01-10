package vladyslava.prazhmovska.dbrgr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SkillsSalaryView {
    private Long id;
    private String name;
    private BigDecimal avgSkillSalary;
    private Long maxSkillSalary;
    private Long hiredApplicants;
    private String specialtyCode;
}

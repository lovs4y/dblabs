package vladyslava.prazhmovska.dbrgr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vladyslava.prazhmovska.dbrgr.core.aspect.ObserveExecutionTime;
import vladyslava.prazhmovska.dbrgr.model.AvgSalaryAndKnowledgeRate;
import vladyslava.prazhmovska.dbrgr.model.RecruiterEfficiencyView;
import vladyslava.prazhmovska.dbrgr.model.SkillPopularityView;
import vladyslava.prazhmovska.dbrgr.model.SkillsSalaryView;
import vladyslava.prazhmovska.dbrgr.model.repository.CVsRepository;
import vladyslava.prazhmovska.dbrgr.model.repository.RecruiterEfficiencyRepository;
import vladyslava.prazhmovska.dbrgr.model.repository.SkillPopularityRepository;
import vladyslava.prazhmovska.dbrgr.model.repository.SkillSalaryRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatisticService {
    private final RecruiterEfficiencyRepository recruiterEfficiencyRepository;
    private final SkillPopularityRepository skillPopularityRepository;
    private final SkillSalaryRepository skillSalaryRepository;
    private final CVsRepository cvsRepository;

    public List<RecruiterEfficiencyView> getRecruitersEfficiency(String recruiterEmail, Long recruiterId) {
        return recruiterEfficiencyRepository.findBy(recruiterEmail, recruiterId);
    }

    @ObserveExecutionTime(comment = "Observing SkillPopularityView find all")
    public List<SkillPopularityView> getSkillsPopularity() {
        return skillPopularityRepository.findAll();
    }

    @ObserveExecutionTime(comment = "Observing SkillsSalaryView find all")
    public List<SkillsSalaryView> getSkillsSalary() {
        return skillSalaryRepository.findAll();
    }

    public AvgSalaryAndKnowledgeRate getAvgSalaryAndKnowledgeRate(String lastName) {
        return cvsRepository.getAvgSalaryAndKnowledgeRateByApplicantLastName(lastName);
    }
}

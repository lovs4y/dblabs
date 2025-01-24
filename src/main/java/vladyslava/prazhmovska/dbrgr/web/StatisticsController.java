package vladyslava.prazhmovska.dbrgr.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vladyslava.prazhmovska.dbrgr.model.AvgSalaryAndKnowledgeRate;
import vladyslava.prazhmovska.dbrgr.model.RecruiterEfficiencyView;
import vladyslava.prazhmovska.dbrgr.model.SkillPopularityView;
import vladyslava.prazhmovska.dbrgr.model.SkillsSalaryView;
import vladyslava.prazhmovska.dbrgr.service.StatisticService;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticService statisticService;

    @GetMapping("/recruiters-efficiency")
    public List<RecruiterEfficiencyView> getRecruitersEfficiency(
            @RequestParam(required = false) String recruiterEmail,
            @RequestParam(required = false) Long recruiterId) {
        return statisticService.getRecruitersEfficiency(recruiterEmail, recruiterId);
    }

    @GetMapping("/skills-popularity")
    public List<SkillPopularityView> getSkillsPopularity() {
        return statisticService.getSkillsPopularity();
    }

    @GetMapping("/skill-salary")
    public List<SkillsSalaryView> getSkillsSalary() {
        return statisticService.getSkillsSalary();
    }

    @GetMapping("/avg-salary-by-last-name")
    public AvgSalaryAndKnowledgeRate getAvgSalaryAndKnowledgeRate(@RequestParam String lastName) {
        return statisticService.getAvgSalaryAndKnowledgeRate(lastName);
    }
}

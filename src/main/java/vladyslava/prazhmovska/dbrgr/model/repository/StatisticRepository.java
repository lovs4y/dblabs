package vladyslava.prazhmovska.dbrgr.model.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vladyslava.prazhmovska.dbrgr.core.aspect.ObserveExecutionTime;
import vladyslava.prazhmovska.dbrgr.core.utils.EntityMapper;
import vladyslava.prazhmovska.dbrgr.core.utils.OptionalUtils;
import vladyslava.prazhmovska.dbrgr.model.RecruiterEfficiencyView;
import vladyslava.prazhmovska.dbrgr.model.SkillPopularityView;
import vladyslava.prazhmovska.dbrgr.model.SkillsSalaryView;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatisticRepository {
    private final JdbcTemplate jdbcTemplate;

    @ObserveExecutionTime
    public List<RecruiterEfficiencyView> getRecruitersEfficiency(String recruiterEmail, Long recruiterId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM recruiter_efficiency_view");
        if (OptionalUtils.atLeastOnePresent(recruiterId, recruiterEmail)) {
            sql.append(" WHERE ");
            if (recruiterId != null) {
                sql.append(String.format("id = %s", recruiterId));
            }

            if (recruiterEmail != null) {
                if (recruiterId != null) {
                    sql.append(" OR ");
                }
                sql.append(String.format("email LIKE '%s'", recruiterEmail));
            }
        }
        sql.append(";");
        return jdbcTemplate.queryForList(sql.toString())
                .stream()
                .map(view -> EntityMapper.map(view, RecruiterEfficiencyView.class))
                .toList();
    }

    @ObserveExecutionTime
    public List<SkillPopularityView> getSkillsPopularity() {
        return jdbcTemplate.queryForList("SELECT * FROM skill_popularity_view")
                .stream()
                .map(view -> EntityMapper.map(view, SkillPopularityView.class))
                .toList();
    }

    @ObserveExecutionTime
    public List<SkillsSalaryView> getSkillsSalary() {
        return jdbcTemplate.queryForList("SELECT * FROM skills_salary_view")
                .stream()
                .map(view -> EntityMapper.map(view, SkillsSalaryView.class))
                .toList();
    }
}

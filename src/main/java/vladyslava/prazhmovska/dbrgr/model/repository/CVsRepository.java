package vladyslava.prazhmovska.dbrgr.model.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vladyslava.prazhmovska.dbrgr.core.aspect.ObserveExecutionTime;
import vladyslava.prazhmovska.dbrgr.model.CV;
import vladyslava.prazhmovska.dbrgr.core.utils.EntityMapper;
import vladyslava.prazhmovska.dbrgr.model.Skill;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class CVsRepository {

    private final JdbcTemplate jdbcTemplate;

    public CVsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Skill> findCVSkills(Long id) {
        String query = "SELECT skills.* FROM skills INNER JOIN cv_skills ON cv_skills.skill_id = skills.id WHERE cv_skills.cv_id = ?";
        logQueryToBeExecuted(query);
        return jdbcTemplate.queryForList(query, id)
                .stream()
                .map(resultMap -> EntityMapper.map(resultMap, Skill.class))
                .toList();
    }

    public CV getById(Long id) {
        String query = "SELECT * FROM cvs WHERE id = ?";
        logQueryToBeExecuted(query);
        try {
            return EntityMapper.map(
                    jdbcTemplate.queryForMap(query, id),
                    CV.class
            );
        } catch (EmptyResultDataAccessException e) {
            log.info("CV with id {} not found", id);
            return null;
        }
    }

    public CV update(CV cv) {
        String query = "UPDATE cvs SET " +
                "first_name = ?, " +
                "mid_name = ?, " +
                "last_name = ?, " +
                "description = ?, " +
                "recruiter_comment = ?, " +
                "specialty_id = ?, " +
                "recruiter_id = ? " +
                "WHERE id = ? " +
                "RETURNING id";
        logQueryToBeExecuted(query);
        Map<String, Object> result = jdbcTemplate.queryForMap(
                query,
                cv.getFirstName(),
                cv.getMidName(),
                cv.getLastName(),
                cv.getDescription(),
                cv.getRecruiterComment(),
                cv.getSpecialtyId(),
                cv.getRecruiterId(),
                cv.getId()
        );
        return this.getById(Long.parseLong(result.get("id").toString()));
    }

    public CV create(CV cv) {
        String query = "INSERT INTO cvs " +
                "(first_name, mid_name, last_name, description, recruiter_comment, specialty_id, recruiter_id, is_hired)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        logQueryToBeExecuted(query);
        Map<String, Object> result = jdbcTemplate.queryForMap(
                query,
                cv.getFirstName(),
                cv.getMidName(),
                cv.getLastName(),
                cv.getDescription(),
                cv.getRecruiterComment(),
                cv.getSpecialtyId(),
                cv.getRecruiterId(),
                cv.getHired()
        );
        return this.getById(Long.parseLong(result.get("id").toString()));
    }

    public void createRandomCVs(Long numberToBeCreated) {
        jdbcTemplate.execute("SELECT test_create_cv(" + numberToBeCreated + ");");
    }

    @ObserveExecutionTime
    public void delete(Long id) {
        String query = "DELETE FROM cvs WHERE id = ?";
        logQueryToBeExecuted(query);
        jdbcTemplate.update(query, id);
    }

    public CV addSkills(Long id, List<Long> skills) {
        try {
            if (skills.isEmpty()) {
                return null;
            }
            StringBuilder sb = new StringBuilder("INSERT INTO cv_skills(cv_id, skill_id) VALUES ");
            for (int i = 0; i < skills.size(); i++) {
                sb.append("(" + id + ", " + skills.get(i) + ")");
                if (i != skills.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append(";");
            logQueryToBeExecuted(sb.toString());
            jdbcTemplate.update(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return getById(id);
    }

    public Long findNumberOfCVsBySkillId(Long skillId) {
        String query = "SELECT COUNT(*) FROM cv_skills WHERE skill_id = ?";
        logQueryToBeExecuted(query);
        Map<String, Object> result = jdbcTemplate.queryForMap(
                query, skillId
        );

        return (Long) result.get("count");
    }

    @ObserveExecutionTime
    public List<CV> findBy(Long skillId, Long recruiterId, Long specialtyId) {
        String query = String.format("SELECT * FROM cvs INNER JOIN cv_skills ON cvs.id = cv_skills.cv_id " +
                "WHERE (%s IS NULL OR cvs.recruiter_id = %s) " +
                "AND (%s IS NULL OR cvs.specialty_id = %s) " +
                "AND (%s IS NULL OR cv_skills.skill_id = %s);",
                recruiterId,
                recruiterId,
                specialtyId,
                specialtyId,
                skillId,
                skillId
        );
        logQueryToBeExecuted(query);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(query);
        return maps.stream()
                .map(cv -> EntityMapper.map(cv, CV.class))
                .toList();
    }

    private void logQueryToBeExecuted(String query) {
        log.info("SQL to be executed: {}", query);
    }
}

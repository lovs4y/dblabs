package vladyslava.prazhmovska.dbrgr.model.repository;

import org.checkerframework.checker.interning.qual.CompareToMethod;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vladyslava.prazhmovska.dbrgr.core.aspect.ObserveExecutionTime;
import vladyslava.prazhmovska.dbrgr.model.AvgSalaryAndKnowledgeRate;
import vladyslava.prazhmovska.dbrgr.model.CV;

import java.util.List;

@Repository
public interface CVsRepository extends JpaRepository<CV, Long> {

    @Query("SELECT test_create_cv(:numberToBeCreated)")
    void createRandomCVs(@Param("numberToBeCreated") Long numberToBeCreated);

    @Query(value = "SELECT COUNT(*) FROM cv_skills WHERE skill_id = :skillId", nativeQuery = true)
    Long findNumberOfCVsBySkillId(@Param("skillId") Long skillId);

    Long countCVBySkillsId(Long skillId);

    @ObserveExecutionTime(comment = "Observing complex of queries: selecting cvs with a couple of params")
    @Query(value = "SELECT * FROM cvs INNER JOIN cv_skills ON cvs.id = cv_skills.cv_id " +
            "WHERE (:recruiterId IS NULL OR cvs.recruiter_id = :recruiterId) " +
            "AND (:specialtyId IS NULL OR cvs.specialty_id = :specialtyId) " +
            "AND (:skillId IS NULL OR cv_skills.skill_id = :skillId)", nativeQuery = true)
    List<CV> findBy(
            @Param("recruiterId") Long recruiterId,
            @Param("specialtyId") Long specialtyId,
            @Param("skillId") Long skillId
    );

    @ObserveExecutionTime(comment = "Observing query from cvs table by last_name")
    List<CV> findByLastName(String lastName, Pageable pageable);

    @ObserveExecutionTime(comment = "Observing select of avg salary and knowledge rate by last name")
    @Query(value = "select floor(avg(salary)) as avg_salary, floor(avg(cv_skills.knowledge_rate)) as avg_knowledge_rate " +
            "from cvs " +
            "inner join cv_skills on cvs.id = cv_skills.cv_id " +
            "where last_name = :lastName " +
            "group by last_name", nativeQuery = true)
    AvgSalaryAndKnowledgeRate getAvgSalaryAndKnowledgeRateByApplicantLastName(@Param("lastName") String lastName);
}

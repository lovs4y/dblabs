package vladyslava.prazhmovska.dbrgr.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vladyslava.prazhmovska.dbrgr.model.SkillsSalaryView;

@Repository
public interface SkillSalaryRepository extends JpaRepository<SkillsSalaryView, Long> {
}

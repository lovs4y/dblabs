package vladyslava.prazhmovska.dbrgr.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vladyslava.prazhmovska.dbrgr.model.SkillPopularityView;

@Repository
public interface SkillPopularityRepository extends JpaRepository<SkillPopularityView, Long> {
}

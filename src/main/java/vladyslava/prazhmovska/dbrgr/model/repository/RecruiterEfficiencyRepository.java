package vladyslava.prazhmovska.dbrgr.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vladyslava.prazhmovska.dbrgr.core.aspect.ObserveExecutionTime;
import vladyslava.prazhmovska.dbrgr.model.RecruiterEfficiencyView;
import java.util.List;

@Repository
public interface RecruiterEfficiencyRepository extends JpaRepository<RecruiterEfficiencyView, Long> {

    @ObserveExecutionTime(comment = "Observing RecruiterEfficiencyView with two params")
    @Query(
            value = "SELECT rev FROM RecruiterEfficiencyView rev " +
                    "WHERE (:recruiterEmail IS NULL OR rev.email like :recruiterEmail) " +
                    "AND (:recruiterId IS NULL OR rev.id = :recruiterId)"
    )
    List<RecruiterEfficiencyView> findBy(
            @Param("recruiterEmail") String recruiterEmail,
            @Param("recruiterId") Long recruiterId
    );
}

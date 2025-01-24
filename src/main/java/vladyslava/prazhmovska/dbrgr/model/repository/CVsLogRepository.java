package vladyslava.prazhmovska.dbrgr.model.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vladyslava.prazhmovska.dbrgr.core.aspect.ObserveExecutionTime;
import vladyslava.prazhmovska.dbrgr.model.CVsLog;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CVsLogRepository extends JpaRepository<CVsLog, Long> {

    @ObserveExecutionTime(comment = "Observing search in cvs_logs by dates between")
    List<CVsLog> findByCreatedAtBetween(
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    );
}

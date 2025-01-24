package vladyslava.prazhmovska.dbrgr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vladyslava.prazhmovska.dbrgr.core.aspect.ObserveExecutionTime;
import vladyslava.prazhmovska.dbrgr.core.exceptions.NotFoundException;
import vladyslava.prazhmovska.dbrgr.core.exceptions.ValidationException;
import vladyslava.prazhmovska.dbrgr.model.CV;
import vladyslava.prazhmovska.dbrgr.model.CVsLog;
import vladyslava.prazhmovska.dbrgr.model.Skill;
import vladyslava.prazhmovska.dbrgr.model.repository.CVsLogRepository;
import vladyslava.prazhmovska.dbrgr.model.repository.CVsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CVsService {

    private final CVsRepository cvRepository;
    private final CVsLogRepository cvsLogRepository;

    public CV getById(Long id) {
        return cvRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CV.class, "id", id));
    }

    @ObserveExecutionTime(comment = "Observing insert operation")
    @Transactional(readOnly = true)
    public CV create(CV cv) {
        cv.setId(null);
        return cvRepository.save(cv);
    }

    public CV update(Long id, CV cv) {
        if (id == null) {
            throw ValidationException.of("NULL_ID_ON_UPDATE")
                    .invalidFieldValue("cv.id", "FIELD_IS_NULL");
        }
        cv.setId(id);

        return cvRepository.save(cv);
    }

    public void delete(Long id) {
        cvRepository.delete(new CV(id));
    }

    public void createRandom(Long numberToBeCreated) {
        cvRepository.createRandomCVs(numberToBeCreated);
    }

    public CV addSkills(Long id, List<Long> skills) {
        CV cv = getById(id);
        cv.setSkills(
                skills.stream().map(skillId -> new Skill(id)).toList()
        );
        cvRepository.save(cv);
        return cv;
    }

    public Long findNumberOfCVsBySkillId(Long skillId) {
        return cvRepository.findNumberOfCVsBySkillId(skillId);
    }

    public List<CV> findBy(Long skillId, Long recruiterId, Long specialtyId) {
        if (specialtyId == null && skillId == null && recruiterId == null) {
            throw new ValidationException("FIND_BY_REQUIRES_ARGUMENT");
        }

        return cvRepository.findBy(skillId, recruiterId, specialtyId);
    }

    public List<CVsLog> findByDate(LocalDateTime from, LocalDateTime to, int page, int pageSize) {
        return cvsLogRepository.findByCreatedAtBetween(from, to, PageRequest.of(page, pageSize));
    }

    @Transactional(readOnly = true)
    public List<CV> findByLastName(String lastName, int page, int size) {
        if (page == 0 || size == 0) {
            throw new ValidationException("PAGINATION_SHOULD_BE_SPECIFIED");
        }
        return cvRepository.findByLastName(lastName, PageRequest.of(page, size));
    }
}

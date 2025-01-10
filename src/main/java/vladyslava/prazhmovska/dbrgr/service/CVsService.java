package vladyslava.prazhmovska.dbrgr.service;

import org.springframework.stereotype.Service;
import vladyslava.prazhmovska.dbrgr.core.aspect.ObserveExecutionTime;
import vladyslava.prazhmovska.dbrgr.model.CV;
import vladyslava.prazhmovska.dbrgr.model.Skill;
import vladyslava.prazhmovska.dbrgr.model.repository.CVsRepository;

import java.util.List;

@Service
public class CVsService {

    private final CVsRepository repository;

    public CVsService(CVsRepository repository) {
        this.repository = repository;
    }

    public CV getById(Long id) {
        CV cv = repository.getById(id);
        if (cv == null) {
            return null;
        }
        List<Skill> cvSkills = repository.findCVSkills(id);
        cv.setSkills(cvSkills);
        return cv;
    }

    public CV create(CV cv) {
        cv.setId(null);
        return repository.create(cv);
    }

    public CV update(Long id, CV cv) {
        if (id == null) {
            throw new RuntimeException("CV_ID_NULL_ON_UPDATE");
        }
        cv.setId(id);

        return repository.update(cv);
    }

    public void delete(Long cvId) {
        repository.delete(cvId);
    }

    public void createRandom(Long numberToBeCreated) {
        repository.createRandomCVs(numberToBeCreated);
    }

    public CV addSkills(Long id, List<Long> skills) {
        repository.addSkills(id, skills);
        return getById(id);
    }

    public Long findNumberOfCVsBySkillId(Long skillId) {
        return repository.findNumberOfCVsBySkillId(skillId);
    }

    @ObserveExecutionTime(comment = "Observing complex of queries: selecting cvs with skills")
    public List<CV> findBy(Long skillId, Long recruiterId, Long specialtyId) {
        if (specialtyId == null && skillId == null && recruiterId == null) {
            throw new RuntimeException("For requesting CVs at least one argument should not be null");
        }

        return repository.findBy(skillId, recruiterId, specialtyId)
                .stream()
                .peek(cv -> cv.setSkills(repository.findCVSkills(cv.getId())))
                .toList();
    }
}

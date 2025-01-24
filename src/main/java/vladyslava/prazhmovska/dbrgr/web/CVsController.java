package vladyslava.prazhmovska.dbrgr.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vladyslava.prazhmovska.dbrgr.model.CV;
import vladyslava.prazhmovska.dbrgr.model.CVsLog;
import vladyslava.prazhmovska.dbrgr.service.CVsService;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping(value = "/cvs", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class CVsController {

    private final CVsService cvsService;

    @GetMapping("/{id}")
    public CV getById(@PathVariable Long id) {
        return cvsService.getById(id);
    }

    @PostMapping
    public CV create(@RequestBody CV cv) {
        return cvsService.create(cv);
    }

    @PutMapping("/{id}")
    public CV update(@PathVariable Long id, @RequestBody CV cv) {
        return cvsService.update(id, cv);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        cvsService.delete(id);
    }

    @PostMapping("/{id}/add-skills")
    public CV addSkills(@PathVariable Long id, @RequestBody List<Long> skillsId) {
        return cvsService.addSkills(id, skillsId);
    }

    // mock method creates random number of CVs
    // with randomly selected skills and recruiter
    @PostMapping("/create-random")
    public void createRandom(@RequestParam(defaultValue = "1000") Long numberToBeCreated) {
        cvsService.createRandom(numberToBeCreated);
    }

    // TODO: to be removed
    @PostMapping("/create-random-with-delay")
    public void createRandomWithDelay(@RequestParam Long numberToBeCreated) {
        try {
            while(numberToBeCreated > 0) {
                cvsService.createRandom(1000L);
                Thread.sleep(60 * 1000);
                numberToBeCreated--;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/count-by-skill/{skillId}")
    public Long findNumberOfCVsBySkillId(@PathVariable Long skillId) {
        return cvsService.findNumberOfCVsBySkillId(skillId);
    }

    @GetMapping("/find-by")
    public List<CV> findBy(@RequestParam(required = false) Long skillId,
                           @RequestParam(required = false) Long recruiterId,
                           @RequestParam(required = false) Long specialtyId
                           ) {
        return cvsService.findBy(skillId, recruiterId, specialtyId);
    }

    @GetMapping("/find-by-lastname")
    public List<CV> findByLastName(
            @RequestParam String lastName,
            @RequestParam int page,
            @RequestParam int pageSize
    ) {
        return cvsService.findByLastName(lastName, page, pageSize);
    }

    @GetMapping("/logs")
    public List<CVsLog> findLogsByDates(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam int page,
            @RequestParam int pageSize
    ) {
        LocalDateTime fromDate = LocalDateTime.parse(from);
        LocalDateTime toDate = LocalDateTime.parse(to);
        return cvsService.findByDate(fromDate, toDate, page, pageSize);
    }
}

package vladyslava.prazhmovska.dbrgr.web;

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
import vladyslava.prazhmovska.dbrgr.service.CVsService;

import java.util.List;

@RequestMapping(value = "/cvs", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class CVsController {

    private final CVsService cvsService;

    public CVsController(CVsService cvsService) {
        this.cvsService = cvsService;
    }

    // add skills to CV
    // catch error when adding unexisting skill to cv

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
}

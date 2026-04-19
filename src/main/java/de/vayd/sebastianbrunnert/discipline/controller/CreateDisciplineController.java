package de.vayd.sebastianbrunnert.discipline.controller;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.api.exceptions.ApiError;
import de.vayd.sebastianbrunnert.discipline.model.Discipline;
import de.vayd.sebastianbrunnert.discipline.model.MetricType;
import de.vayd.sebastianbrunnert.discipline.repository.DisciplineRepository;
import de.vayd.sebastianbrunnert.files.exceptions.FileException;
import de.vayd.sebastianbrunnert.files.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("discipline")
@PreAuthorize("@access.isAdmin()")
public class CreateDisciplineController {

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private StorageService storageService;

    @PostMapping
    @JsonView(DisciplinesController.class)
    public ResponseEntity createDiscipline(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam("name") String name,
            @RequestParam("metric") String metric,
            @RequestParam("metricType")MetricType metricType,
            @RequestParam(value = "cover", required = false)MultipartFile file
    ) throws ApiError {
        Discipline discipline = id == null ? new Discipline() : disciplineRepository.findById(id).orElse(new Discipline());
        discipline.setName(name).setMetric(metric).setMetricType(metricType);
        if (file != null) {
            try {
                discipline.setCover(storageService.uploadResource(file.getInputStream(), file.getOriginalFilename()));
            } catch (FileException | IOException e) {
                throw new ApiError().setMessage("Failed to upload cover").setLevel(ApiError.Level.INQUIRER).setDetails("cover");
            }
        }
        if(discipline.getCover() == null) {
            throw new ApiError().setMessage("Failed to upload cover").setLevel(ApiError.Level.INQUIRER).setDetails("cover");
        }

        return ResponseEntity.ok(disciplineRepository.save(discipline));
    }

}

package de.vayd.sebastianbrunnert.discipline.controller;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.discipline.repository.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("disciplines")
@PreAuthorize("@access.isUser()")
public class DisciplinesController {

    @Autowired
    private DisciplineRepository disciplineRepository;

    @GetMapping
    @JsonView(DisciplinesController.class)
    public ResponseEntity getDisciplines() {
        return ResponseEntity.ok(disciplineRepository.findAll());
    }

}

package de.vayd.sebastianbrunnert.discipline.controller;

import de.vayd.sebastianbrunnert.discipline.repository.DisciplineRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("discipline")
@PreAuthorize("@access.isAdmin()")
public class DeleteDisciplineController {

    @Autowired
    private DisciplineRepository disciplineRepository;

    @DeleteMapping
    @Transactional
    public ResponseEntity deleteDiscipline(
            @RequestParam(value = "id") Long id
    ) {
        disciplineRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}

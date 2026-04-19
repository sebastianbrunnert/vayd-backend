package de.vayd.sebastianbrunnert.discipline.repository;

import de.vayd.sebastianbrunnert.discipline.model.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
}

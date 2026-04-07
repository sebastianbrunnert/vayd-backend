package de.vayd.sebastianbrunnert.files.repository;

import de.vayd.sebastianbrunnert.files.model.CustomFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomFileRepository extends JpaRepository<CustomFile, Long> {

}

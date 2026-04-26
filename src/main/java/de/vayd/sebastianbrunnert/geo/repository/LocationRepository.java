package de.vayd.sebastianbrunnert.geo.repository;

import de.vayd.sebastianbrunnert.geo.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, String> {
}

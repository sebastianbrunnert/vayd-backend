package de.vayd.sebastianbrunnert.authentication.repository;

import de.vayd.sebastianbrunnert.authentication.model.Registerable;
import de.vayd.sebastianbrunnert.authentication.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegisterableRepository<T extends Registerable> extends JpaRepository<T, Long> {

    @Query("SELECT r FROM #{#entityName} r")
    List<T> findAll();

    @Query("SELECT r FROM registerables r WHERE r.email = :email AND TYPE(r) = :type")
    Optional<T> findByEmailAndType(String email, Class<? extends T> type);

}



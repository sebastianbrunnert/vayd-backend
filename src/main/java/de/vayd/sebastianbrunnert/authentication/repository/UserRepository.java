package de.vayd.sebastianbrunnert.authentication.repository;

import de.vayd.sebastianbrunnert.authentication.model.User;

import java.util.Optional;

public interface UserRepository extends RegisterableRepository<User> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}

package de.vayd.sebastianbrunnert.authentication.model;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.authentication.controller.GoogleController;
import de.vayd.sebastianbrunnert.authentication.controller.LoginController;
import de.vayd.sebastianbrunnert.authentication.controller.RegisterController;
import de.vayd.sebastianbrunnert.authentication.controller.WhoAmIController;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This class is the base class for all registerable entities. It contains the basic information for a registerable entity.
 * It holds properties for identification and authentication.
 * It is stored in the database. All entities that inherit from this class will be stored as separate tables and will be joined with this table.
 */
@Data
@Accessors(chain = true)
@Entity(name = "registerables")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Registerable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({WhoAmIController.class})
    private Long id;

    private String hash;

    private String salt;

    @JsonView({WhoAmIController.class})
    private String email;

    @JsonView({WhoAmIController.class, RegisterController.class, LoginController.class, GoogleController.class})
    private String name;
}



package de.vayd.sebastianbrunnert.authentication.model;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.authentication.controller.LoginController;
import de.vayd.sebastianbrunnert.authentication.controller.RegisterController;
import de.vayd.sebastianbrunnert.authentication.controller.WhoAmIController;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity(name = "users")
@Data
@Accessors(chain = true)
public class User extends Registerable {

    @JsonView({WhoAmIController.class, LoginController.class, RegisterController.class})
    private boolean admin = false;

    private boolean google = false;

}


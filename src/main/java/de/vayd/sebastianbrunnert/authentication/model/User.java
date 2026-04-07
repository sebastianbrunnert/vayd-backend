package de.vayd.sebastianbrunnert.authentication.model;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.authentication.controller.WhoAmIController;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity(name = "users")
@Data
@Accessors(chain = true)
public class User extends Registerable {

    @JsonView(WhoAmIController.class)
    private boolean admin = false;

}


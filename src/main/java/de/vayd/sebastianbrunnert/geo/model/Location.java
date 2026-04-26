package de.vayd.sebastianbrunnert.geo.model;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.profile.controller.EditProfileController;
import de.vayd.sebastianbrunnert.profile.controller.ProfileController;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity(name = "locations")
@Data
@Accessors(chain = true)
@JsonView({ProfileController.class, EditProfileController.class})
public class Location {

    @Id
    private String id;

    @ManyToOne
    private Location parent;

    private String name;

}

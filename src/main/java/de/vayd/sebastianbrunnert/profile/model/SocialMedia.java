package de.vayd.sebastianbrunnert.profile.model;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.profile.controller.EditProfileController;
import de.vayd.sebastianbrunnert.profile.controller.ProfileController;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SocialMedia {

    @JsonView({ProfileController.class, EditProfileController.class})
    private SocialMediaType type;

    @JsonView({ProfileController.class, EditProfileController.class})
    private String link;

}

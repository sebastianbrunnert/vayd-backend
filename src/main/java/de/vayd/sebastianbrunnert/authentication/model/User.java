package de.vayd.sebastianbrunnert.authentication.model;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.authentication.controller.*;
import de.vayd.sebastianbrunnert.files.model.CustomFile;
import de.vayd.sebastianbrunnert.geo.model.Location;
import de.vayd.sebastianbrunnert.profile.controller.EditProfileController;
import de.vayd.sebastianbrunnert.profile.controller.ProfileController;
import de.vayd.sebastianbrunnert.profile.model.SocialMedia;
import de.vayd.sebastianbrunnert.profile.model.SocialMediaType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(name = "users")
@Data
@Accessors(chain = true)
public class User extends Registerable {

    public User() {
        super();
    }

    @JsonView({WhoAmIController.class, LoginController.class, RegisterController.class, GoogleController.class})
    private boolean admin = false;

    @JsonView({ProfileController.class, EditProfileController.class})
    private boolean google = false;

    @JsonView({ProfileController.class, EditProfileController.class})
    @OneToOne
    private CustomFile avatar;

    @JsonView({ProfileController.class, EditProfileController.class})
    @ManyToOne
    private Location location;

    @JsonView({ProfileController.class, EditProfileController.class})
    private String slogan;

    @ElementCollection
    private Map<SocialMediaType, String> socialMediaLinks = new HashMap<>();

    @JsonView({ProfileController.class, EditProfileController.class})
    public List<SocialMedia> getSocialMedias() {
        return Arrays.stream(SocialMediaType.values()).map(type -> new SocialMedia().setType(type).setLink(socialMediaLinks.get(type))).toList();
    }

    public void chooseSlogan() {
        this.slogan = switch ((int) (Math.random() * 5)) {
            case 0 -> "Don't stop until you're proud.";
            case 1 -> "The only bad workout is the one that didn't happen.";
            case 2 -> "Sweat is just fat crying.";
            case 3 -> "Strive for progress, not perfection.";
            case 4 -> "Push yourself, because no one else is going to do it for you.";
            default -> "No pain, no gain.";
        };
    }

}


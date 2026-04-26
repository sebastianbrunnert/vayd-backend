package de.vayd.sebastianbrunnert.profile.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.profile.controller.EditProfileController;
import de.vayd.sebastianbrunnert.profile.controller.ProfileController;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonView({ProfileController.class, EditProfileController.class})
public enum SocialMediaType {
    INSTAGRAM("Instagram", "instagram"),
    LINKEDIN("LinkedIn", "linkedin"),
    YOUTUBE("YouTube", "youtube"),
    TIKTOK("TikTok", "tiktok"),
    LINK("Link", "link");

    public String getId() {
        return this.name();
    }

    private final String name;
    private final String icon;

}
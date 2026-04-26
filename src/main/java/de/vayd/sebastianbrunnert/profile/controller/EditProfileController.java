package de.vayd.sebastianbrunnert.profile.controller;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.api.exceptions.ApiError;
import de.vayd.sebastianbrunnert.authentication.filter.AccessComponent;
import de.vayd.sebastianbrunnert.authentication.model.User;
import de.vayd.sebastianbrunnert.authentication.repository.UserRepository;
import de.vayd.sebastianbrunnert.files.exceptions.FileException;
import de.vayd.sebastianbrunnert.geo.model.Location;
import de.vayd.sebastianbrunnert.geo.service.NominatimService;
import de.vayd.sebastianbrunnert.profile.model.SocialMediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("profile")
@PreAuthorize("@access.isUser()")
public class EditProfileController {

    @Autowired
    private AccessComponent accessComponent;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NominatimService nominatimService;

    @PostMapping
    @JsonView(EditProfileController.class)
    public ResponseEntity editProfile(
            @RequestParam("name") String name,
            @RequestParam("slogan") String slogan,
            @RequestParam(value = "avatar", required = false)MultipartFile avatar,
            @RequestParam(value = "removeAvatar", defaultValue = "false") boolean removeAvatar,
            @RequestParam(value = "location", required = false) String location
    ) throws ApiError {
        User user = this.accessComponent.getUser();
        user.setSlogan(slogan).setName(name);
        if(removeAvatar) {
            user.setAvatar(null);
        } else if(avatar != null) {
            try {
                user.setAvatar(this.accessComponent.getStorageService().uploadResource(avatar.getInputStream(), avatar.getOriginalFilename()));
            } catch (FileException | IOException e) {
                throw new ApiError().setMessage("Failed to upload avatar").setDetails("avatar");
            }
        }

        if (location != null) {
            try {
                Location resolved = nominatimService.resolveAndSave(location);
                if (resolved == null) throw new ApiError().setMessage("Location not found").setDetails("location");
                user.setLocation(resolved);
            } catch (Exception e) {
                throw new ApiError().setMessage("Location not found").setDetails("location");
            }
        } else {
            user.setLocation(null);
        }

        this.userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("social")
    public ResponseEntity editSocialMedia(@RequestParam("type")SocialMediaType type, @RequestParam("link") String link) {
        User user = this.accessComponent.getUser();
        user.getSocialMediaLinks().put(type, link);
        this.userRepository.save(user);
        return ResponseEntity.ok(user);
    }

}

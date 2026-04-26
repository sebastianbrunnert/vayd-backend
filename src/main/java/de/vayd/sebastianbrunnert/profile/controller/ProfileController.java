package de.vayd.sebastianbrunnert.profile.controller;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.api.exceptions.NotFoundException;
import de.vayd.sebastianbrunnert.authentication.model.User;
import de.vayd.sebastianbrunnert.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @JsonView(ProfileController.class)
    public ResponseEntity getProfile(
            @RequestParam("id") Long id
    ) throws NotFoundException {
        User user = this.userRepository.findById(id).orElseThrow(NotFoundException::new);
        if (user.getSlogan() == null) user.chooseSlogan();
        return ResponseEntity.ok(user);
    }

}

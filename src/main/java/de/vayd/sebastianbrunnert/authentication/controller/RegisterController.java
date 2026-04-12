package de.vayd.sebastianbrunnert.authentication.controller;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.api.exceptions.ApiError;
import de.vayd.sebastianbrunnert.authentication.model.Registerable;
import de.vayd.sebastianbrunnert.authentication.model.Role;
import de.vayd.sebastianbrunnert.authentication.model.User;
import de.vayd.sebastianbrunnert.authentication.model.intern.AuthenticationContext;
import de.vayd.sebastianbrunnert.authentication.repository.RegisterableRepository;
import de.vayd.sebastianbrunnert.authentication.repository.UserRepository;
import de.vayd.sebastianbrunnert.authentication.services.HashingGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @JsonView(RegisterController.class)
    public ResponseEntity register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("hash") String hash
    ) throws ApiError {
        if(this.userRepository.existsByEmail(email)) {
            throw new ApiError().setMessage("There is already a user with this email address.").setDetails("email").setLevel(ApiError.Level.INQUIRER);
        }
        if(!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ApiError().setMessage("The email address is not valid.").setDetails("email").setLevel(ApiError.Level.INQUIRER);
        }

        HashingGenerator hashingGenerator = new HashingGenerator(hash);
        User user = (User) new User().setEmail(email).setName(name).setHash(hashingGenerator.generate()).setSalt(hashingGenerator.getSalt());
        this.userRepository.save(user);

        return ResponseEntity.ok(new AuthenticationContext().setRegisterable(user));
    }

}

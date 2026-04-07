package de.vayd.sebastianbrunnert.authentication.controller;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.api.exceptions.ApiError;
import de.vayd.sebastianbrunnert.authentication.model.Registerable;
import de.vayd.sebastianbrunnert.authentication.model.Role;
import de.vayd.sebastianbrunnert.authentication.model.intern.AuthenticationContext;
import de.vayd.sebastianbrunnert.authentication.repository.RegisterableRepository;
import de.vayd.sebastianbrunnert.authentication.services.HashingGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {


    @Autowired
    private RegisterableRepository<Registerable> registerableRepository;

    @PostMapping
    @JsonView(LoginController.class)
    public ResponseEntity login(
            @RequestParam("email") String email,
            @RequestParam("hash") String hash
    ) throws ApiError {
        ApiError error = new ApiError().setMessage("Falsche Nutzerdaten.").setDetails("email").setLevel(ApiError.Level.INQUIRER);
        Registerable registerable = this.registerableRepository.findByEmailAndType(email, Role.USER.getClazz()).orElseThrow(() -> error);

        if(!registerable.getHash().equals(new HashingGenerator(hash, registerable.getSalt()).generate())) {
            throw error;
        }

        return ResponseEntity.ok(new AuthenticationContext().setRegisterable(registerable));
    }

}

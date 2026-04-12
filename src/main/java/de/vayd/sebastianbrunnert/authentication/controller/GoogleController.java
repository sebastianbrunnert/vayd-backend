package de.vayd.sebastianbrunnert.authentication.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import de.vayd.sebastianbrunnert.api.exceptions.ApiError;
import de.vayd.sebastianbrunnert.authentication.model.User;
import de.vayd.sebastianbrunnert.authentication.model.intern.AuthenticationContext;
import de.vayd.sebastianbrunnert.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("google")
public class GoogleController {

    @Autowired
    private UserRepository userRepository;

    private final GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private final NetHttpTransport httpTransport = new NetHttpTransport();
    private final String clientId;
    private final String clientSecret;

    public GoogleController(
            @Value("${google.client-id}") String clientId,
            @Value("${google.client-secret}") String clientSecret
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @PostMapping
    @JsonView(GoogleController.class)
    public ResponseEntity google(
            @RequestParam("code") String code
    ) throws ApiError {
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    httpTransport,
                    jsonFactory,
                    clientId,
                    clientSecret,
                    code,
                    "postmessage"
            ).execute();

            GoogleIdToken idToken = tokenResponse.parseIdToken();
            GoogleIdToken.Payload payload = idToken.getPayload();

            String email = payload.getEmail();

            User existingUser = this.userRepository.findByEmail(email).orElse(null);
            if(existingUser != null) {
                if(existingUser.isGoogle()) {
                    return ResponseEntity.ok(new AuthenticationContext().setRegisterable(existingUser));
                } else {
                    throw new ApiError().setMessage("There is already a user with this email address.").setLevel(ApiError.Level.ALERT);
                }
            }

            User user = (User) new User().setGoogle(true).setEmail(email).setName((String) payload.get("name"));
            this.userRepository.save(user);

            return ResponseEntity.ok(new AuthenticationContext().setRegisterable(user));
        } catch (IOException e) {
            throw new ApiError().setMessage("GOOGLE_AUTHENTICATION_FAILED").setLevel(ApiError.Level.ALERT);
        }
    }

}

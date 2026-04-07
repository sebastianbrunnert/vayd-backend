package de.vayd.sebastianbrunnert.authentication.model.intern;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.Vayd;
import de.vayd.sebastianbrunnert.authentication.controller.LoginController;
import de.vayd.sebastianbrunnert.authentication.model.Registerable;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class AuthenticationContext {

    // The registerable
    private Registerable registerable;

    // The expiration time of the JWT token in milliseconds.
    // If the value is -1, the token never expires.
    @JsonView({LoginController.class})
    private Long expiration = -1L;

    @JsonView({LoginController.class})
    public String getToken() {
        JwtBuilder builder = Jwts.builder().setAudience("REGISTERABLE").setSubject(this.registerable.getId().toString());
        if (this.expiration != -1) {
            builder.setExpiration(new Date(System.currentTimeMillis() + this.expiration));
        }

        return builder.signWith(SignatureAlgorithm.HS512, Vayd.getInstance().getKey()).compact();
    }

}


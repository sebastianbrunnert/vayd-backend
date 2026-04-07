package de.vayd.sebastianbrunnert.authentication.model.intern;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.authentication.controller.WhoAmIController;
import de.vayd.sebastianbrunnert.authentication.model.Registerable;
import de.vayd.sebastianbrunnert.authentication.model.Role;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * This class represents an active authentication session. Every user that has logged in receives an instance of this class.
 */
@Data
@Accessors(chain = true)
public class CustomAuthentication implements Authentication {

    @JsonView(WhoAmIController.class)
    private Role role;

    @JsonView(WhoAmIController.class)
    private Registerable registerable;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return "";
    }
}


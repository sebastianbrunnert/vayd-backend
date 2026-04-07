package de.vayd.sebastianbrunnert.authentication.model.intern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.vayd.sebastianbrunnert.files.model.CustomFile;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@Accessors(chain = true)
public class CustomFileAuthentication implements Authentication {

    @JsonIgnore
    private CustomFile customFile;

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

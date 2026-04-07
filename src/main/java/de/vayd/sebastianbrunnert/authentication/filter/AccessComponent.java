package de.vayd.sebastianbrunnert.authentication.filter;

import de.vayd.sebastianbrunnert.Vayd;
import de.vayd.sebastianbrunnert.authentication.model.Registerable;
import de.vayd.sebastianbrunnert.authentication.model.Role;
import de.vayd.sebastianbrunnert.authentication.model.User;
import de.vayd.sebastianbrunnert.authentication.model.intern.CustomAuthentication;
import de.vayd.sebastianbrunnert.authentication.model.intern.CustomFileAuthentication;
import de.vayd.sebastianbrunnert.authentication.repository.RegisterableRepository;
import de.vayd.sebastianbrunnert.files.model.CustomFile;
import de.vayd.sebastianbrunnert.files.repository.CustomFileRepository;
import de.vayd.sebastianbrunnert.files.services.StorageService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * This component is used to get details of the current authentication context.
 * For example at controllers, you can use @PreAuthorize("@access.is('...')")
 */
@Component("access")
public class AccessComponent {

    @Autowired @Getter
    private RegisterableRepository<Registerable> registerableRepository;

    @Autowired @Getter
    private StorageService storageService;

    @Autowired
    private CustomFileRepository customFileRepository;

    private JwtParser parser;

    /**
     * Creates a new instance of the AccessComponent.
     * It initializes the parser with the key from the main instance.
     */
    public AccessComponent() {
        this.parser = Jwts.parser().setSigningKey(Vayd.getInstance().getKey());
    }

    /**
     * @param token The JWT token
     * @return The authentication context of the token
     */
    public Authentication getAuthentication(String token) {
        try {
            Claims claims = this.parser.parseClaimsJws(token).getBody();


            if(claims.getAudience().equals("FILE")) {
                CustomFile customFile = this.customFileRepository.findById(Long.valueOf(claims.getSubject())).orElse(null);
                if(customFile == null)
                    return null;
                return new CustomFileAuthentication().setCustomFile(customFile);
            }

            if(claims.getAudience().equals("REGISTERABLE")) {
                Registerable registerable = this.registerableRepository.findById(Long.valueOf(claims.getSubject())).get();
                for(Role role : Role.values()) {
                    if(role.getClazz() != null && role.getClazz().isInstance(registerable)) {
                        return new CustomAuthentication().setRegisterable(registerable).setRole(role);
                    }
                }

                return new CustomAuthentication().setRegisterable(registerable);
            }

            // If no role was found, return null
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method is mainly used for @PreAuthorize annotations.
     * @param role The role to check
     * @return If the current authentication context has the given role
     */
    public boolean is(Role role) {
        if(SecurityContextHolder.getContext().getAuthentication() == null || !(SecurityContextHolder.getContext().getAuthentication() instanceof CustomAuthentication))
            return false;
        return ((CustomAuthentication) SecurityContextHolder.getContext().getAuthentication()).getRole() == role;
    }

    public User getUser() {
        if(!this.is(Role.USER)) return null;
        return (User) ((CustomAuthentication) SecurityContextHolder.getContext().getAuthentication()).getRegisterable();
    }

    public boolean isFile() {
        return SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication() instanceof CustomFileAuthentication;
    }

}

package de.vayd.sebastianbrunnert.authentication.filter;

import de.vayd.sebastianbrunnert.authentication.model.Role;
import de.vayd.sebastianbrunnert.authentication.model.intern.CustomAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

/**
 * This filter is used to authorize the user with the JWT token.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private AccessComponent accessComponent;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AccessComponent accessComponent) {
        super(authenticationManager);
        this.accessComponent = accessComponent;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // By default, the user is a guest
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthentication().setRole(Role.GUEST));

        // Check if the token is in the header or in the request parameters
        String token = request.getHeader("Authorization");

        if (token == null) {
            token = request.getParameter("authorization");
        } else if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if(token == null) {
            chain.doFilter(request, response);
            return;
        }

        // Get the authentication context from the token
        Authentication authentication = this.accessComponent.getAuthentication(token);

        // If the authentication context is not null, set it to the security context
        if(authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

}


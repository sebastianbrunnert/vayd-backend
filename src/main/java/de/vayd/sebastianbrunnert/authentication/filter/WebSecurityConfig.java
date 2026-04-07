package de.vayd.sebastianbrunnert.authentication.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;


/**
 * This class is used to configure the security of the application.
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private AccessComponent accessComponent;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // Add custom filter to authorize the user with the JWT token
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), this.accessComponent);
        http.addFilterBefore(jwtAuthorizationFilter, BearerTokenAuthenticationFilter.class);

        // Configure CORS and CSRF
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowedHeaders(List.of("*"));

        http.cors().configurationSource(request -> corsConfiguration);
        http.csrf().disable();

        http.headers().frameOptions().disable();

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}

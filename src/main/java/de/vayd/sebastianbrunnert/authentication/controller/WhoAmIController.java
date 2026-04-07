package de.vayd.sebastianbrunnert.authentication.controller;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.authentication.filter.AccessComponent;
import de.vayd.sebastianbrunnert.authentication.model.intern.CustomAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class represents the endpoint for WhoAMI requests.
 */
@RestController
@RequestMapping("whoami")
public class WhoAmIController {

    @Autowired
    private AccessComponent accessComponent;

    @GetMapping
    @JsonView(WhoAmIController.class)
    public ResponseEntity whoAmI() {
        CustomAuthentication authentication = (CustomAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(authentication);
    }

}
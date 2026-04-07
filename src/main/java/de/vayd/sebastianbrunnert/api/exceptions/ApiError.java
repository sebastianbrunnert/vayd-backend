package de.vayd.sebastianbrunnert.api.exceptions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This class represents an API error. It holds standardised error messages the frontend can use to display errors.
 * Because it extends Exception, we have to be sensible with Jackson annotations.
 */
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.NONE
)
@Data
@Accessors(chain = true)
public class ApiError extends Exception {

    // The message of the error. Simple alert messages
    @JsonProperty
    private String message;

    // The level of the error. Is used to determine which level of the frontend the error should be managed
    @JsonProperty
    private Level level;

    // More detailed information about the error (e.g. which input field caused the error)
    @JsonProperty
    private Object details;

    public enum Level {
        // Nothing should happen with this error and the request should be taken as if nothing happened
        IGNORE,
        // The user should be logged out
        LOGOUT,
        // A simple alert should be shown
        ALERT,
        // The error should be thrown and explicitly handled
        INTERN,
        // The error should be handled by a form
        INQUIRER
    }

}

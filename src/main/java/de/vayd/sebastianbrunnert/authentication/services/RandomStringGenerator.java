package de.vayd.sebastianbrunnert.authentication.services;

import java.security.SecureRandom;

/**
 * This class is used to generate a random string of a given length
 */
public class RandomStringGenerator {

    // All characters that can be used in the string
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyz-_ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private int length;

    /**
     * Constructor of RandomStringGenerator that generates a random string
     * @param length The length of the string to generate
     */
    public RandomStringGenerator(int length) {
        this.length = length;
    }

    /**
     * @return The generated random string
     */
    public String generate() {
        return new SecureRandom().ints(16, 0, CHARS.length()).mapToObj(CHARS::charAt).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

}


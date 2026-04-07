package de.vayd.sebastianbrunnert.authentication.services;

import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * This class is used to generate a hash of a password with a salt
 */
@Data
public class HashingGenerator {

    // All characters that can be used in the salt
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyz-_ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String password;
    private String salt;

    /**
     * Constructor of HashingGenerator that generates a random salt
     * @param password The password to hash
     */
    public HashingGenerator(String password) {
        this.password = password;

        // Generate a random salt
        this.salt = new RandomStringGenerator(16).generate();
    }

    /**
     * Constructor of HashingGenerator that uses a given salt
     * @param password password
     * @param salt salt
     */
    public HashingGenerator(String password, String salt) {
        this.password = password;
        this.salt = salt;
    }

    /**
     * This method generates a hash for the password. If something goes wrong, the password will be returned
     *
     * @return The generated hash of the password with salt
     */
    public String generate() {
        // Source: https://stackoverflow.com/questions/33085493/how-to-hash-a-password-with-sha-512-in-java
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(this.salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(this.password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            return this.password;
        }
    }

}


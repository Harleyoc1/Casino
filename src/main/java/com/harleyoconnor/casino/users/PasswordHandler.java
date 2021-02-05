package com.harleyoconnor.casino.users;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * @author Harley O'Connor
 */
public final class PasswordHandler {

    private static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int COST = 16;
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 128;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final SecretKeyFactory KEY_FACTORY;

    static {
        try {
            KEY_FACTORY = SecretKeyFactory.getInstance(HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            // This should never happen, but but if it does throw an exception.
            throw new RuntimeException("Couldn't find algorithm \"" + HASH_ALGORITHM + "\". This should never happen.");
        }
    }

    private String password;

    public PasswordHandler(String password) {
        this.password = password;
    }

    public PasswordHandler hash () {
        // Create the salt object, with the amount of bytes set to the cost constant. The more bytes, the more secure.
        byte[] salt = new byte[COST];

        // Generate random bytes for the salt.
        SECURE_RANDOM.nextBytes(salt);

        // Create KeySpec object from the password and salt with the iteration count and key length constants.
        KeySpec spec = new PBEKeySpec(this.password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);

        byte[] hash;

        try {
            // Generate the hash from the secret key factory.
            hash = KEY_FACTORY.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Unable to generate hash.");
        }

        // Use Base64 encoder to store the bytes as a string.
        this.password = Base64.getEncoder().encodeToString(hash);
        return this;
    }

    public boolean authenticate (String passwordIn) {
        // Decode the Base64 string into bytes.
        byte[] hash = Base64.getUrlDecoder().decode(passwordIn);

        return false;
    }

    public String getPassword() {
        return password;
    }

}

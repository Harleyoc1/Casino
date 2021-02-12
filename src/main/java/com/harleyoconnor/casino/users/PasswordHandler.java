package com.harleyoconnor.casino.users;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * This class handles holding, hashing, and authenticating passwords.
 *
 * @author Harley O'Connor
 */
public final class PasswordHandler {

    private static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int SIZE = 128;
    private static final int COST = 65536;

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

    /**
     * Hashes the the password stored by this {@link PasswordHandler}.
     *
     * @return This {@link PasswordHandler}.
     */
    public PasswordHandler hash () {
        // Create the salt object, with the amount of bytes set to the size constant.
        byte[] salt = new byte[SIZE / 8];

        // Generate random bytes for the salt.
        SECURE_RANDOM.nextBytes(salt);

        // Generate the hashed string from the password char array and random salt.
        byte[] hashedString = this.generateHash(this.password.toCharArray(), salt);

        byte[] hash = new byte[salt.length + hashedString.length];

        // Copy the salt and hashedString to the hash.
        System.arraycopy(salt, 0, hash, 0, salt.length);
        System.arraycopy(hashedString, 0, hash, salt.length, hashedString.length);

        // Use Base64 encoder to store the bytes as a string.
        this.password = Base64.getEncoder().encodeToString(hash);

        return this;
    }

    /**
     * Authenticates the given password from the hash stored.
     *
     * @param passwordIn The password inputted.
     * @return True if the password matches the hash stored, false if not.
     */
    public boolean authenticate (String passwordIn) {
        // Decode the Base64 string into bytes.
        byte[] hash = Base64.getDecoder().decode(this.password);

        // Get the salt from the hash.
        byte[] salt = Arrays.copyOfRange(hash, 0, SIZE / 8);

        // Generate a new hash from the salt stored and password input.
        byte[] hashFromIn = this.generateHash(passwordIn.toCharArray(), salt);

        // Check the hashes match.

        int zero = 0;
        for (int idx = 0; idx < hashFromIn.length; ++idx) {
            // Adds bits to "zero" if any were left over from an "OR" operation on the two hashes.
            zero |= hash[salt.length + idx] ^ hashFromIn[idx];
        }

        // If zero was changed (some of the bits didn't match up), the passwords don't match.
        return zero == 0;
    }

    private byte[] generateHash (char[] charsToHash, byte[] salt) {
        // Create KeySpec object from the characters and salt with the cost and key length constants.
        final KeySpec spec = new PBEKeySpec(charsToHash, salt, COST, SIZE);

        try {
            // Generate the hash from the secret key factory.
            return KEY_FACTORY.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Unable to generate hash.");
        }
    }

    public String getPassword() {
        return password;
    }

}

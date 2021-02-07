package com.harleyoconnor.casino.users;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * This class handles holding, hashing, and authenticating passwords.
 *
 * @author Harley O'Connor
 */
public final class PasswordHandler {

    private static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int SIZE = 16;
    private static final int COST = 65536;
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
        // Create the salt object, with the amount of bytes set to the size constant.
        byte[] salt = new byte[SIZE];

        // Generate random bytes for the salt.
        SECURE_RANDOM.nextBytes(salt);
        System.out.println(Base64.getEncoder().encodeToString(salt));

        // Generate the hash from the password char array and random salt.
        byte[] hash = this.generateHash(this.password.toCharArray(), salt);

        // Use Base64 encoder to store the bytes as a string.
        this.password = Base64.getEncoder().encodeToString(hash);
        return this;
    }

    public boolean authenticate (String passwordIn) {
//        // Decode the Base64 string into bytes.
//        byte[] hash = Base64.getDecoder().decode(this.password);
//        byte[] salt = Arrays.copyOfRange(hash, 0, SIZE);
//        byte[] hashFromIn = this.generateHash(passwordIn.toCharArray(), salt);
//
//        int zero = 0;
//        for (int idx = 0; idx < hashFromIn.length; ++idx) {
//            zero |= hash[SIZE + idx] ^ hashFromIn[idx];
//        }
//
//        return zero == 0;
        return true; // Temporary until this is fixed.
    }

    private byte[] generateHash (char[] charsToHash, byte[] salt) {
        // Create KeySpec object from the characters and salt with the cost and key length constants.
        final KeySpec spec = new PBEKeySpec(charsToHash, salt, COST, KEY_LENGTH);

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

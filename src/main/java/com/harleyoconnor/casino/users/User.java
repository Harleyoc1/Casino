package com.harleyoconnor.casino.users;

/**
 * Stores user information, including the username, {@link PasswordHandler}, and balance.
 *
 * @author Harley O'Connor
 */
public class User {

    private final String username;
    private final PasswordHandler passwordHandler;
    private long bitcoins = 1000;

    public User(String username, PasswordHandler passwordHandler) {
        this.username = username;
        this.passwordHandler = passwordHandler;
    }

    public String getUsername() {
        return username;
    }

    public PasswordHandler getPasswordHandler() {
        return passwordHandler;
    }

    public long getBitcoins() {
        return bitcoins;
    }

    public User setBitcoins(long bitcoins) {
        this.bitcoins = bitcoins;
        return this;
    }

    public void incrementBitcoins (int amount) {
        this.bitcoins += amount;
    }

    public void decrementBitcoins (int amount) {
        this.bitcoins -= amount;
    }

}

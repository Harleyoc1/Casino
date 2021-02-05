package com.harleyoconnor.casino.users;

/**
 * @author Harley O'Connor
 */
public final class User {

    private final String username;
    private final PasswordHandler passwordHandler;
    private int bitcoins = 1000;

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

    public int getBitcoins() {
        return bitcoins;
    }

    public void setBitcoins(int bitcoins) {
        this.bitcoins = bitcoins;
    }

    public void incrementBitcoins (int amount) {
        this.bitcoins += amount;
    }

    public void decrementBitcoins (int amount) {
        this.bitcoins -= amount;
    }

}

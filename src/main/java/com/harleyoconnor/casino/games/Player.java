package com.harleyoconnor.casino.games;

import com.harleyoconnor.casino.users.User;
import com.harleyoconnor.casino.users.Users;

import java.util.Optional;

/**
 * An extension of the {@link User} class that also contains variables needed during games.
 *
 * @author Harley O'Connor
 */
public final class Player extends User {

    /** The amount the user has bet on the game. */
    private int amountBet;
    private final int originalAmountBet;

    public Player(User user, int amountBet) {
        super(user.getUsername(), user.getPasswordHandler());
        this.amountBet = amountBet;
        this.originalAmountBet = amountBet;
    }

    /**
     * Halves the current bet.
     */
    public void halfBet () {
        this.amountBet /= 2;
    }

    public int getAmountBet() {
        return amountBet;
    }

    public void setAmountBet(int amountBet) {
        this.amountBet = amountBet;
    }

    public int getOriginalAmountBet() {
        return originalAmountBet;
    }

    /**
     * Resets the amount bet to the original bet amount for this {@link Player} object.
     *
     * @return This {@link Player} object.
     */
    public Player resetBet () {
        this.amountBet = this.originalAmountBet;
        return this;
    }

    /**
     * @return The actual {@link User} object from the registry, if it still exists.
     */
    private Optional<User> getActualUser () {
        return Users.find(this.username);
    }

    /**
     * Takes the bet amount from the User.
     */
    public void betLost () {
        // Gets the actual user object.
        Optional<User> userOptional = this.getActualUser();

        if (userOptional.isEmpty())
            return;

        // Decrement the user's balance by the amount bet.
        userOptional.get().decrementBitcoins(this.amountBet);
        // Update the users Json file.
        Users.updated();
    }

    public void betWon () {
        // Gets the actual user object.
        Optional<User> userOptional = this.getActualUser();

        if (userOptional.isEmpty())
            return;

        // Increment the user's balance by the amount bet.
        userOptional.get().incrementBitcoins(this.amountBet);
        // Update the users Json file.
        Users.updated();
    }

}

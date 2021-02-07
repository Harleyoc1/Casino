package com.harleyoconnor.casino.games;

import com.harleyoconnor.casino.users.User;

/**
 * An extension of the {@link User} class that also contains variables needed during games.
 *
 * @author Harley O'Connor
 */
public final class Player extends User {

    /** The amount the user has bet on the game. */
    private final int amountBet;

    public Player(User user, int amountBet) {
        super(user.getUsername(), user.getPasswordHandler());
        this.amountBet = amountBet;
    }

    public int getAmountBet() {
        return amountBet;
    }

}

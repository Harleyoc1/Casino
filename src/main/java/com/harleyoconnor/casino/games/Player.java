package com.harleyoconnor.casino.games;

import com.harleyoconnor.casino.users.User;

/**
 * @author Harley O'Connor
 */
public final class Player extends User {

    private final int amountBet;

    public Player(User user, int amountBet) {
        super(user.getUsername(), user.getPasswordHandler());
        this.amountBet = amountBet;
    }

    public int getAmountBet() {
        return amountBet;
    }

}

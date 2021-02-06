package com.harleyoconnor.casino.games;

import com.harleyoconnor.casino.games.blackjack.BlackJack;
import com.harleyoconnor.casino.games.blackjack.BlackJackHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harley O'Connor
 */
public final class Games {

    public static final List<GameHolder<?>> GAMES = new ArrayList<>();

    public static final GameHolder<BlackJack> BLACKJACK = new BlackJackHolder();

    static {
        GAMES.add(BLACKJACK);
    }

}

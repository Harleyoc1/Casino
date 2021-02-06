package com.harleyoconnor.casino.games.blackjack;

import com.harleyoconnor.casino.games.GameHolder;
import com.harleyoconnor.casino.games.Player;
import com.harleyoconnor.casino.menus.MenuScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public final class BlackJackHolder extends GameHolder<BlackJack> {

    public BlackJackHolder() {
        super("BlackJack");
    }

    @Override
    public BlackJack construct(Stage stage, Scene scene, MenuScreen previousScreen, Player player) {
        return new BlackJack(stage, scene, previousScreen, player);
    }

}

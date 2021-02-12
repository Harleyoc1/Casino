package com.harleyoconnor.casino.games.blackjack;

import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.games.GameHolder;
import com.harleyoconnor.casino.games.Player;
import com.harleyoconnor.casino.menus.MenuScreen;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public final class BlackJackHolder extends GameHolder<BlackJack> {

    public BlackJackHolder() {
        super("BlackJack");
    }

    @Override
    public BlackJack construct(Casino casino, Stage stage, Scene scene, StackPane parentView, MenuScreen previousScreen, Player player) {
        return new BlackJack(casino, stage, scene, parentView, previousScreen, player);
    }

}

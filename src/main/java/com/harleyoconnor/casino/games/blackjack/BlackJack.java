package com.harleyoconnor.casino.games.blackjack;

import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.games.Game;
import com.harleyoconnor.casino.games.Games;
import com.harleyoconnor.casino.games.Player;
import com.harleyoconnor.casino.menus.MenuScreen;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public final class BlackJack extends Game {

    public BlackJack(Casino casino, Stage stage, Scene scene, MenuScreen previousScreen, Player player) {
        super(casino, stage, scene, previousScreen, player, Games.BLACKJACK);
    }

    @Override
    protected Pane setupScreen() {
        final Pane layout = new StackPane();

        return layout;
    }

}

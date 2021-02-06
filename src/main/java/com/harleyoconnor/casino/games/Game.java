package com.harleyoconnor.casino.games;

import com.harleyoconnor.casino.menus.MenuScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public abstract class Game extends MenuScreen {

    protected final Player player;
    protected final GameHolder<?> gameHolder;

    public Game(Stage stage, Scene scene, MenuScreen previousScreen, Player player, GameHolder<?> gameHolder) {
        super(stage, scene, previousScreen);
        this.player = player;
        this.gameHolder = gameHolder;
    }

    @Override
    public String getTitle() {
        return this.gameHolder.getName();
    }

}

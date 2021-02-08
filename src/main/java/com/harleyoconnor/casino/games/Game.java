package com.harleyoconnor.casino.games;

import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.menus.MenuScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A super-class for game screens.
 *
 * @author Harley O'Connor
 */
public abstract class Game extends MenuScreen {

    /** The player object for the current game. */
    protected final Player player;
    /** The game holder for the current game. */
    protected final GameHolder<?> gameHolder;

    public Game(Casino casino, Stage stage, Scene scene, MenuScreen previousScreen, Player player, GameHolder<?> gameHolder) {
        super(casino, stage, scene, previousScreen, false);
        this.player = player;
        this.gameHolder = gameHolder;
    }

    @Override
    public void show() {
        this.layout = this.setupScreen();
        super.show();
    }

    @Override
    public String getTitle() {
        return this.gameHolder.getName();
    }

}

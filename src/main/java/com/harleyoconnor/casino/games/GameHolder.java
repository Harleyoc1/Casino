package com.harleyoconnor.casino.games;

import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.menus.MenuScreen;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Holds information about each game, and handles constructing them.
 *
 * @author Harley O'Connor
 */
public abstract class GameHolder<T extends Game> {

    protected final String name;

    public GameHolder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * This should construct the {@link Game} object.
     *
     * @param casino The {@link Casino} instance.
     * @param stage The primary {@link Stage}.
     * @param scene The main {@link Scene}.
     * @param parentView The parent {@link StackPane} view.
     * @param previousScreen The previous {@link MenuScreen}.
     * @param player The {@link Player} object.
     * @return The {@link Game} object.
     */
    public abstract T construct (Casino casino, Stage stage, Scene scene, StackPane parentView, MenuScreen previousScreen, Player player);

}

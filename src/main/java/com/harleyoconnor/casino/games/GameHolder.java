package com.harleyoconnor.casino.games;

import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.menus.MenuScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public abstract class GameHolder<T extends Game> {

    private final String name;

    public GameHolder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract T construct (Casino casino, Stage stage, Scene scene, MenuScreen previousScreen, Player player);

}

package com.harleyoconnor.casino.menus;

import com.harleyoconnor.casino.Casino;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public abstract class MenuScreen {

    protected final Stage stage;
    protected final Scene scene;
    protected final MenuScreen previousScreen;
    protected final Pane layout;

    public MenuScreen(Stage stage, Scene scene, MenuScreen previousScreen) {
        this.stage = stage;
        this.scene = scene;
        this.previousScreen = previousScreen;

        Casino.getInstance().setTitle(this.getTitle());

        this.layout = this.setupScreen();

        this.show();
    }

    public void show () {
        this.scene.setRoot(this.layout);
    }

    protected abstract Pane setupScreen ();

    protected abstract String getTitle ();

}

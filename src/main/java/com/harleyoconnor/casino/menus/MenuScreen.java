package com.harleyoconnor.casino.menus;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public abstract class MenuScreen {

    protected final Stage stage;
    protected final Scene scene;
    protected final Pane previousLayout;
    protected final Pane layout;

    public MenuScreen(Stage stage, Scene scene, Pane previousLayout) {
        this.stage = stage;
        this.scene = scene;
        this.previousLayout = previousLayout;

        this.stage.setTitle(this.stage.getTitle() + " - " + this.getTitle());

        this.layout = this.setupScreen();

        this.scene.setRoot(this.layout);
    }

    protected abstract Pane setupScreen ();

    protected abstract String getTitle ();

}

package com.harleyoconnor.casino.menus;

import com.harleyoconnor.casino.Casino;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Handles the Account stage, which is used for changing passwords and logging out.
 *
 * @author Harley O'Connor
 */
public final class AccountStageHandler {

    private final Stage stage = new Stage();
    private final StackPane view = new StackPane();
    private final Scene scene = new Scene(this.view, 300, 200);

    private final Casino casino;
    private final MenuScreen mainScreen;
    private final GamesMenuScreen otherOpenScreen;

    public AccountStageHandler(Casino casino, GamesMenuScreen otherOpenScreen) {
        this.casino = casino;
        this.setupStage();
        this.setupScene();
        this.mainScreen = new AccountScreen(this.casino, this, this.stage, this.scene, this.view);
        this.otherOpenScreen = otherOpenScreen;
    }

    /**
     * Sets up main properties of the stage.
     */
    private void setupStage () {
        this.stage.setTitle("Account");
        this.stage.setScene(this.scene);
        this.stage.setResizable(false);
        this.stage.setOnCloseRequest(event -> this.close());
    }

    /**
     * Sets up main properties of the scene.
     */
    private void setupScene () {
        this.casino.addDefaultStylesheet(this.scene);
    }

    /**
     * Shows the stage and screen.
     */
    public void show () {
        this.mainScreen.show();
        this.stage.show();
    }

    /**
     * Closes the stage.
     */
    public void close () {
        // Reset the account stage so that the other screen knows it has been closed.
        this.otherOpenScreen.setAccountStage(null);
        this.stage.close();
    }

    public Stage getStage() {
        return stage;
    }

    public MenuScreen getOtherOpenScreen() {
        return otherOpenScreen;
    }

}

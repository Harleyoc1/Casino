package com.harleyoconnor.casino;

import com.harleyoconnor.casino.menus.SignInScreen;
import com.harleyoconnor.casino.menus.SignUpScreen;
import com.harleyoconnor.casino.users.User;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public final class Casino extends Application {

    private static Casino INSTANCE;

    private User currentUser;

    private Stage primaryStage;
    private Scene primaryScene;

    @Override
    public void start(Stage primaryStage) {
        INSTANCE = this;

        this.primaryStage = primaryStage;
        this.primaryScene = new Scene(new StackPane());

        this.setupBasicProperties();

        new SignUpScreen(this.primaryStage, this.primaryScene, null);

        primaryStage.setScene(this.primaryScene);
        primaryStage.show();
    }

    /**
     * Sets up basic properties for the main {@link Stage}.
     */
    private void setupBasicProperties () {
        this.primaryStage.setTitle("Casino");
        this.primaryStage.setHeight(300);
        this.primaryStage.setWidth(500);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public static void main (String[] args) {
        launch(args);
    }

    public static Casino getInstance() {
        return INSTANCE;
    }

}

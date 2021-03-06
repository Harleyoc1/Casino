package com.harleyoconnor.casino;

import com.harleyoconnor.casino.builders.StackPaneBuilder;
import com.harleyoconnor.casino.menus.GamesMenuScreen;
import com.harleyoconnor.casino.menus.SignInScreen;
import com.harleyoconnor.casino.textures.cards.Cards;
import com.harleyoconnor.casino.users.User;
import com.harleyoconnor.casino.users.Users;
import com.harleyoconnor.javautilities.FileUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public final class Casino extends Application {

    /** Holds the instance for {@link Casino}, the main class of the program. */
    private static Casino INSTANCE;

    /** Holds the user object for the user currently signed in. */
    private User currentUser;

    private Stage primaryStage;
    private Scene primaryScene;
    private StackPane primaryView;

    @Override
    public void start(Stage primaryStage) {
        INSTANCE = this;

        Users.getFromFile();
        Cards.loadAndRegisterCards();

        this.primaryStage = primaryStage;
        this.primaryView = StackPaneBuilder.create().build();
        this.primaryScene = new Scene(this.primaryView);

        // Add default stylesheet to the scene.
        this.addDefaultStylesheet(this.primaryScene);

        this.setupBasicProperties();

        // Creates and shows the sign in screen.
        new SignInScreen(this, this.primaryStage, this.primaryScene, this.primaryView, null).show();
//        this.setCurrentUser(Users.find("Harleyoc1").get());
//        new GamesMenuScreen(this, this.primaryStage, this.primaryScene, this.primaryView, null).show();

        primaryStage.setScene(this.primaryScene);
        primaryStage.show();
    }

    /**
     * Sets up basic properties for the main {@link Stage}.
     */
    private void setupBasicProperties () {
        // Set minimum and default widths and heights.
        this.primaryStage.setMinWidth(AppConstants.MIN_WIDTH);
        this.primaryStage.setMinHeight(AppConstants.MIN_HEIGHT);
        this.primaryStage.setWidth(AppConstants.DEFAULT_WIDTH);
        this.primaryStage.setHeight(AppConstants.DEFAULT_HEIGHT);

        // Set the title of the scene.
        this.setTitle();
    }

    /**
     * Sets the default title of the window - which is just "Casino".
     */
    public void setTitle () {
        this.setTitle("");
    }

    /**
     * Sets the title of the window with the given subtitle. For example, it would be set "Casino - Sign In"
     * if <tt>subtitle</tt> were "Sign In".
     *
     * @param subtitle The subtitle to set.
     */
    public void setTitle (String subtitle) {
        this.primaryStage.setTitle(AppConstants.APP_NAME + (subtitle.length() > 0 ? " - " + subtitle : ""));
    }

    /**
     * @return The current signed in user.
     */
    public User getCurrentUser() {
        return this.currentUser;
    }

    /**
     * Sets the current signed in user.
     *
     * @param currentUser The user object for the user who has signed in.
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Adds the default stylesheet to the given {@link Scene} object.
     *
     * @param scene The {@link Scene} object.
     */
    public void addDefaultStylesheet (Scene scene) {
        scene.getStylesheets().add(AppConstants.FILE_PREFIX + FileUtils.getFile(AppConstants.DEFAULT_STYLESHEET_PATH).getPath());
    }

    public static void main (String[] args) {
        launch(args);
    }

    /**
     * @return The main {@link Casino} instance.
     */
    public static Casino getInstance() {
        return INSTANCE;
    }

}

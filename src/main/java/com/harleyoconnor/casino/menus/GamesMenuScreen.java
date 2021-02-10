package com.harleyoconnor.casino.menus;

import com.harleyoconnor.casino.AppConstants;
import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.builders.*;
import com.harleyoconnor.casino.games.Game;
import com.harleyoconnor.casino.games.GameHolder;
import com.harleyoconnor.casino.games.Games;
import com.harleyoconnor.casino.games.Player;
import com.harleyoconnor.casino.users.User;
import com.harleyoconnor.casino.utils.InterfaceUtils;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Harley O'Connor
 */
public final class GamesMenuScreen extends MenuScreen {

    // Style classes.
    private static final String GAME_BUTTON_CLASS = "game-button";
    private static final String GAME_BUTTON_SELECTED_CLASS = "game-button-selected";

    private final User user;

    private Map<GameHolder<?>, Button> gameButtons;

    private final TextFieldBuilder<TextField> betAmountField = TextFieldBuilder.createTextField().placeholder("BTC").body().fixWidth(60);
    private final LabelBuilder<Label> errorLabel = LabelBuilder.create().wrapText().body();
    private final ButtonBuilder<Button> startGameButton = ButtonBuilder.create().text("Start").body().fixWidth(60).onAction(this::onStartGamePressed);

    private final HBoxBuilder<HBox> errorLabelContainer = HBoxBuilder.create().add(this.errorLabel.build()).centre().padding(6);

    private final VBoxBuilder<VBox> buttonSelectedGroup = VBoxBuilder.create().add(
            HBoxBuilder.create().add(this.betAmountField.build()).centre().padding(6).build(),
            HBoxBuilder.create().add(this.startGameButton.build()).centre().padding(6).build());

    private final Timeline errorClearer = TimelineBuilder.create().keyFrame(10000, new SimpleBooleanProperty(false), false)
            .onFinished(event -> this.buttonSelectedGroup.remove(this.errorLabelContainer.build())).build();

    private final VBoxBuilder<VBox> foregroundView = VBoxBuilder.create();

    private GameHolder<?> selectedGame = null;

    public GamesMenuScreen(Casino casino, Stage stage, Scene scene, MenuScreen previousScreen) {
        super(casino, stage, scene, previousScreen, false);
        this.user = casino.getCurrentUser();
    }

    @Override
    public void show() {
        this.layout = this.setupScreen();
        super.show();
    }

    @Override
    protected Pane setupScreen() {
        StackPaneBuilder<StackPane> view = StackPaneBuilder.create(); // Create main view.

        this.setupGameButtons(); // Sets up the game buttons.

        // Creates an HBox for the game buttons.
        final HBox gameButtonHBox = HBoxBuilder.create().add(this.gameButtons.values().toArray(new Button[0])).centre()
                .padding(15, 0, 15, 0).build();

        Label balanceLabel = LabelBuilder.create().text("Balance: " + this.user.getBitcoins() + " BTC").body().build();
        VBox accountView = VBoxBuilder.create().add(balanceLabel).padding().build();

        this.foregroundView.add(gameButtonHBox).centre().padding(25);

        // Creates and returns a VBox as the main layout.
        return view.add(HBoxBuilder.create().add(InterfaceUtils.createHorizontalSpacer(), accountView).build(), this.foregroundView.build()).build();
    }

    /**
     * Fills out the <tt>gameButtons</tt> map for each game in the registry.
     */
    private void setupGameButtons () {
        // This must be initialised here because it is called from setupScreen which is called from the super-class's constructor.
        this.gameButtons = new HashMap<>();

        Games.GAMES.forEach(gameHolder -> {
            final Button gameButton = ButtonBuilder.create().text(gameHolder.getName()).styleClasses(GAME_BUTTON_CLASS, AppConstants.INVISIBLE_BUTTON_CLASS)
                    .title().fixWidthHeight(150).onAction(event -> this.onGamePressed(gameHolder)).build();
            this.gameButtons.put(gameHolder, gameButton);
        });
    }

    /**
     * Executes when one of the game buttons is pressed, (artificially) changing the button's "state" to selected.
     *
     * @param gameHolder The game holder for the selected game.
     */
    private void onGamePressed(GameHolder<?> gameHolder) {
        final GameHolder<?> previousGameSelected = this.selectedGame;

        if (previousGameSelected == null || !previousGameSelected.equals(gameHolder)) {
            // Select game button.
            this.selectedGame = gameHolder;

            // Add bet amount field and start game button (if they aren't already present).
            this.foregroundView.insertIfNotPresent(this.buttonSelectedGroup.build(), this.foregroundView.build().getChildren().size() - 1);

            this.betAmountField.focus();

            // Add the game selected style class.
            this.gameButtons.get(gameHolder).getStyleClass().add(GAME_BUTTON_SELECTED_CLASS);
        } else {
            // Deselect game button.
            this.selectedGame = null;

            // Remove bet amount field and start game button.
            this.foregroundView.remove(this.buttonSelectedGroup.build());
        }

        // Remove the game selected style class if there was previously a button selected.
        if (previousGameSelected != null)
            this.gameButtons.get(previousGameSelected).getStyleClass().removeAll(GAME_BUTTON_SELECTED_CLASS);
    }

    /**
     * Executes when the start button is pressed, getting the bet amount from the field if it is a number
     * and meets the minimum bet value, and they can afford it. It then creates the {@link Player} object
     * from this and starts the selected {@link Game}.
     *
     * @param event The {@link ActionEvent}.
     */
    private void onStartGamePressed(ActionEvent event) {
        if (this.selectedGame == null)
            return;

        int betAmount;

        // Try to obtain bet amount if text of the field is an integer.
        try {
            betAmount = Integer.parseInt(this.betAmountField.build().getText());
        } catch (NumberFormatException e) {
            this.error("You must enter an amount to bet.");
            return;
        }

        // Ensure the user is betting at least the minimum bet amount.
        if (betAmount < AppConstants.MIN_BET) {
            this.error("You must bet at least " + AppConstants.MIN_BET + " bitcoins.");
            return;
        }

        // Ensure the user has enough money to make the bet.
        if (this.user.getBitcoins() < betAmount) {
            // TODO: Display user's balance in top right of screen.
            this.error("You do not have enough to bet.");
            return;
        }

        this.selectedGame.construct(this.casino, this.stage, this.scene, this, new Player(this.user, betAmount)).show();
    }

    private void error (String description) {
        this.errorLabel.text(description);
        this.buttonSelectedGroup.insertIfNotPresent(this.errorLabelContainer.build(), 1);

        /* The error clearer will remove the error label after 10 seconds. When there is an error, we want
           to stop the current clearer and play it again. */
        this.errorClearer.stop();
        this.errorClearer.play();
    }

    @Override
    public String getTitle() {
        return "Games";
    }

}

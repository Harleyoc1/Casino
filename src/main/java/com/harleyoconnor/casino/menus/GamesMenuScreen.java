package com.harleyoconnor.casino.menus;

import com.harleyoconnor.casino.AppConstants;
import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.builders.*;
import com.harleyoconnor.casino.games.Game;
import com.harleyoconnor.casino.games.GameHolder;
import com.harleyoconnor.casino.games.Games;
import com.harleyoconnor.casino.games.Player;
import com.harleyoconnor.casino.textures.cards.Cards;
import com.harleyoconnor.casino.users.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

    private TextField betAmountField;
    private Label errorLabel;

    private VBox buttonSelectedGroup;

    private GameHolder<?> selectedGame = null;

    public GamesMenuScreen(Casino casino, Stage stage, Scene scene, MenuScreen previousScreen) {
        super(casino, stage, scene, previousScreen);
        this.user = casino.getCurrentUser();
    }

    @Override
    protected Pane setupScreen() {
        this.setupGameButtons(); // Sets up the game buttons.

        // Creates an HBox for the game buttons.
        final HBox gameButtonHBox = HBoxBuilder.createHBox().add(this.gameButtons.values().toArray(new Button[0])).centre()
                .padding(15, 0, 15, 0).build();

        // Sets up the nodes that display when a game is selected.
        this.betAmountField = TextFieldBuilder.createTextField().placeholder("BTC").body().fixWidth(60).build();
        this.errorLabel = LabelBuilder.createLabel().wrapText().body().build();
        final Button startGameButton = ButtonBuilder.createButton().text("Start").body().fixWidth(60).onAction(this::onStartGamePressed).build();

        // Groups the nodes for when a game is selected into a VBox and manages padding and alignment for them.
        this.buttonSelectedGroup = VBoxBuilder.createVBox().add(
                HBoxBuilder.createHBox().add(this.betAmountField).centre().padding(6).build(),
                HBoxBuilder.createHBox().add(this.errorLabel).centre().padding(6).build(),
                HBoxBuilder.createHBox().add(startGameButton).centre().padding(6).build()).build();

        // Creates and returns a VBox as the main layout.
        return VBoxBuilder.createVBox().add(gameButtonHBox).centre().padding(25).build();
    }

    /**
     * Fills out the <tt>gameButtons</tt> map for each game in the registry.
     */
    private void setupGameButtons () {
        // This must be initialised here because it is called from setupScreen which is called from the super-class's constructor.
        this.gameButtons = new HashMap<>();

        Games.GAMES.forEach(gameHolder -> {
            final Button gameButton = ButtonBuilder.createButton().text(gameHolder.getName()).styleClasses(GAME_BUTTON_CLASS, AppConstants.INVISIBLE_BUTTON_CLASS)
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
        final ObservableList<Node> layoutChildren = this.layout.getChildren();
        final GameHolder<?> previousGameSelected = this.selectedGame;

        if (previousGameSelected == null || !previousGameSelected.equals(gameHolder)) {
            // Select game button.
            this.selectedGame = gameHolder;

            // Add bet amount field and start game button (if they aren't already present).
            if (!layoutChildren.contains(this.buttonSelectedGroup))
                layoutChildren.add(layoutChildren.size() - 1, this.buttonSelectedGroup);

            this.betAmountField.requestFocus();

            // Add the game selected style class.
            this.gameButtons.get(gameHolder).getStyleClass().add(GAME_BUTTON_SELECTED_CLASS);
        } else {
            // Deselect game button.
            this.selectedGame = null;

            // Remove bet amount field and start game button.
            layoutChildren.removeAll(this.buttonSelectedGroup);
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
            betAmount = Integer.parseInt(this.betAmountField.getText());
        } catch (NumberFormatException e) {
            this.errorLabel.setText("You must enter an amount to bet.");
            return;
        }

        // Ensure the user is betting at least the minimum bet amount.
        if (betAmount < AppConstants.MIN_BET) {
            this.errorLabel.setText("You must bet at least " + AppConstants.MIN_BET + " bitcoins.");
            return;
        }

        // Ensure the user has enough money to make the bet.
        if (this.user.getBitcoins() < betAmount) {
            // TODO: Display user's balance in top right of screen.
            this.errorLabel.setText("You do not have enough to bet.");
            return;
        }

        this.selectedGame.construct(this.casino, this.stage, this.scene, this, new Player(this.user, betAmount)).show();
    }

    @Override
    public String getTitle() {
        return "Games";
    }

}

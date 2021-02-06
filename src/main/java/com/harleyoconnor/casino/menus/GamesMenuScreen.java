package com.harleyoconnor.casino.menus;

import com.harleyoconnor.casino.AppConstants;
import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.builders.ButtonBuilder;
import com.harleyoconnor.casino.builders.TextFieldBuilder;
import com.harleyoconnor.casino.games.GameHolder;
import com.harleyoconnor.casino.games.Games;
import com.harleyoconnor.casino.games.Player;
import com.harleyoconnor.casino.utils.InterfaceUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    private Map<GameHolder<?>, Button> gameButtons;

    private TextField betAmountField;

    public GamesMenuScreen(Stage stage, Scene scene, MenuScreen previousScreen) {
        super(stage, scene, previousScreen);
    }

    @Override
    protected Pane setupScreen() {
        final Pane layout = new StackPane();

        final VBox vBox = new VBox();
        final HBox gameButtons = new HBox();

        this.setupGameButtons();

        this.betAmountField = TextFieldBuilder.createTextField().placeholder("Amount to Bet").build();

        InterfaceUtils.centreElementsHorizontally(InterfaceUtils.addElementsToPane(gameButtons, this.gameButtons.values().toArray(new Button[0])));
        InterfaceUtils.centreElementsVertically(InterfaceUtils.addElementsToPane(vBox, gameButtons));

        layout.setPadding(new Insets(25));
        InterfaceUtils.addElementsToPane(layout, vBox);

        return layout;
    }

    private void setupGameButtons () {
        this.gameButtons = new HashMap<>();

        Games.GAMES.forEach(gameHolder -> {
            final Button gameButton = ButtonBuilder.createButton().text(gameHolder.getName()).styleClasses(AppConstants.INVISIBLE_BUTTON_CLASS)
                    .fixWidthHeight(150).onAction(event -> gameButtonPressed(event, gameHolder)).build();
            this.gameButtons.put(gameHolder, gameButton);
        });
    }

    private void gameButtonPressed (ActionEvent event, GameHolder<?> gameHolder) {
        gameHolder.construct(this.stage, this.scene, this, new Player(Casino.getInstance().getCurrentUser(), 6)).show();
    }

    @Override
    public String getTitle() {
        return "Games";
    }

}

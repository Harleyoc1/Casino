package com.harleyoconnor.casino.menus;

import com.harleyoconnor.casino.AppConstants;
import com.harleyoconnor.casino.builders.ButtonBuilder;
import com.harleyoconnor.casino.utils.InterfaceUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public final class GamesMenuScreen extends MenuScreen {

    public GamesMenuScreen(Stage stage, Scene scene, MenuScreen previousScreen) {
        super(stage, scene, previousScreen);
    }

    @Override
    protected Pane setupScreen() {
        final Pane layout = new StackPane();

        final VBox vBox = new VBox();
        final HBox gameButtons = new HBox();

        final Button gameButton = ButtonBuilder.createButton().text("BlackJack").styleClasses(AppConstants.INVISIBLE_BUTTON_CLASS).build();

        InterfaceUtils.centreElementsHorizontally(InterfaceUtils.addElementsToPane(gameButtons, gameButton));
        InterfaceUtils.centreElementsVertically(InterfaceUtils.addElementsToPane(vBox, gameButtons));

        layout.setPadding(new Insets(25));
        InterfaceUtils.addElementsToPane(layout, vBox);

        return layout;
    }

    @Override
    protected String getTitle() {
        return "Games";
    }

}

package com.harleyoconnor.casino.menus;

import com.harleyoconnor.casino.utils.InterfaceUtils;
import com.harleyoconnor.casino.builders.TextFieldBuilder;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public final class SignInScreen extends MenuScreen {

    public SignInScreen(Stage stage, Scene scene, Pane previousLayout) {
        super(stage, scene, previousLayout);
    }

    @Override
    protected Pane setupScreen() {
        Pane layout = new StackPane();
        VBox vBox = new VBox(5);

        TextField usernameField = TextFieldBuilder.createTextField().placeholder("Username").build();
        TextField passwordField = TextFieldBuilder.createPasswordField().placeholder("Password").build();

        Button signInButton = new Button();
        signInButton.setOnAction(this::onSignInPress);

        vBox.setPadding(new Insets(25));
        vBox.getChildren().addAll(InterfaceUtils.createVerticalSpacer(), usernameField, passwordField, signInButton, InterfaceUtils.createVerticalSpacer());

        layout.getChildren().addAll(vBox);
        return layout;
    }

    private void onSignInPress (ActionEvent event) {

    }

    @Override
    protected String getTitle() {
        return "Sign In";
    }

}

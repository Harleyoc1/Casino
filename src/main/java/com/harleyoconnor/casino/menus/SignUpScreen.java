package com.harleyoconnor.casino.menus;

import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.builders.LabelBuilder;
import com.harleyoconnor.casino.users.PasswordHandler;
import com.harleyoconnor.casino.users.User;
import com.harleyoconnor.casino.users.Users;
import com.harleyoconnor.casino.utils.InterfaceUtils;
import com.harleyoconnor.casino.builders.TextFieldBuilder;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public final class SignUpScreen extends MenuScreen {

    private TextField usernameField;
    private TextField passwordField;
    private Label errorLabel;

    public SignUpScreen(Stage stage, Scene scene, Pane previousLayout) {
        super(stage, scene, previousLayout);
    }

    @Override
    protected Pane setupScreen() {
        Pane layout = new StackPane();

        VBox vBox = new VBox(5);

        this.usernameField = TextFieldBuilder.createTextField().placeholder("Username").build();
        this.passwordField = TextFieldBuilder.createPasswordField().placeholder("Password").build();
        this.errorLabel = LabelBuilder.createLabel().wrapText().build();

        Button signUpButton = new Button();
        signUpButton.setOnAction(this::onSignUpPres);

        vBox.setPadding(new Insets(25));
        vBox.getChildren().addAll(InterfaceUtils.createVerticalSpacer(), this.usernameField, this.passwordField, signUpButton, InterfaceUtils.createVerticalSpacer());

        layout.getChildren().addAll(vBox);

        return layout;
    }

    private void onSignUpPres(ActionEvent event) {
        String username = this.usernameField.getText();

        if (Users.doesUsernameExist(username)) {
            this.errorLabel.setText("Username already exists.");
            return;
        }

        PasswordHandler passwordHandler = new PasswordHandler(this.passwordField.getText()).hash();

        System.out.println(passwordHandler.getPassword());

        Casino.getInstance().setCurrentUser(new User(username, passwordHandler));
    }

    @Override
    protected String getTitle() {
        return "Sign Up";
    }

}

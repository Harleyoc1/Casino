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

        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(this::onSignUpPres);

        vBox.setPadding(new Insets(25));
        vBox.getChildren().addAll(InterfaceUtils.createVerticalSpacer(), this.usernameField, this.passwordField, signUpButton, this.errorLabel, InterfaceUtils.createVerticalSpacer());

        layout.getChildren().addAll(vBox);

        return layout;
    }

    private void onSignUpPres(ActionEvent event) {
        String username = this.usernameField.getText();

        // Make sure they have actually entered a username.
        if (username.length() < 1) {
            this.errorLabel.setText("You must enter a username.");
            return;
        }

        // Make sure the username doesn't already exist.
        if (Users.doesUsernameExist(username)) {
            this.errorLabel.setText("Username already exists.");
            return;
        }

        String password = this.passwordField.getText();

        // Make sure the password is at least the minimum length.
        if (password.length() < Users.MIN_PASSWORD_LENGTH) {
            this.errorLabel.setText("Password must have at least " + Users.MIN_PASSWORD_LENGTH + " characters.");
            return;
        }

        // Put password into a handler and hash it.
        PasswordHandler passwordHandler = new PasswordHandler(this.passwordField.getText()).hash();
        // Create new user object.
        User user = new User(username, passwordHandler);

        Users.register(user); // Register the user.
        Casino.getInstance().setCurrentUser(user); // Set new user to current one.
    }

    @Override
    protected String getTitle() {
        return "Sign Up";
    }

}

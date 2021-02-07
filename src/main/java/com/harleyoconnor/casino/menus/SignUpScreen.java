package com.harleyoconnor.casino.menus;

import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.builders.*;
import com.harleyoconnor.casino.users.PasswordHandler;
import com.harleyoconnor.casino.users.User;
import com.harleyoconnor.casino.users.Users;
import com.harleyoconnor.casino.utils.InterfaceUtils;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public final class SignUpScreen extends MenuScreen {

    private TextField usernameField;
    private TextField passwordField;
    private TextField confirmPasswordField;
    private Label errorLabel;

    public SignUpScreen(Casino casino, Stage stage, Scene scene, MenuScreen previousScreen) {
        super(casino, stage, scene, previousScreen);
    }

    @Override
    protected Pane setupScreen() {
        // Create title label.
        final Label titleLabel = LabelBuilder.createLabel().text("Sign Up").title().wrapText().build();

        // Create fields for username, password, and password confirmation.
        this.usernameField = TextFieldBuilder.createTextField().placeholder("Username").build();
        this.passwordField = TextFieldBuilder.createPasswordField().placeholder("Password").build();
        this.confirmPasswordField = TextFieldBuilder.createPasswordField().placeholder("Confirm Password").build();

        // Create error label, for when the user enters details wrong - such as entering an existing username.
        this.errorLabel = LabelBuilder.createLabel().wrapText().build();

        // Create sign up button - this will attempt to sign the user up based on the entered data.
        Button signUpButton = ButtonBuilder.createButton().text("Sign Up").onAction(this::onSignUpPress).build();

        // Create sign in button - this will redirect the user to the sign in screen.
        Button signInButton = ButtonBuilder.createButton().text("Sign In").onAction(this::onSignInPress).build();

        // Create vertical box and add the content to it.
        VBox vBox = VBoxBuilder.createVBox().add(titleLabel, this.usernameField, this.passwordField, this.confirmPasswordField,
                HBoxBuilder.createHBox().add(signInButton, InterfaceUtils.createHorizontalSpacer(), signUpButton).build(), this.errorLabel)
                .spacing().padding(25).fixWidth(300).build();

        // Create and return horizontal box with vertical box of content in the centre.
        return HBoxBuilder.createHBox().add(vBox).centre().build();
    }

    /**
     * Executes when the sign up button is pressed. Checks if a username was entered and doesn't already exist,
     * if the passwords in the password and confirm password match, and if the minimum password length was met.
     * If any of those conditions weren't met, the <tt>errorLabel</tt> is updated and we return, otherwise we
     * create and register the new user and open the game menu.
     *
     * @param event The {@link ActionEvent}.
     */
    private void onSignUpPress(ActionEvent event) {
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

        // Make sure the password is the same as text in the password confirm.
        if (!password.equals(this.confirmPasswordField.getText())) {
            this.errorLabel.setText("Passwords do not match.");
            return;
        }

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
        this.casino.setCurrentUser(user); // Set new user to current one.
        new GamesMenuScreen(this.casino, this.stage, this.scene, this).show();
    }

    /**
     * Executes when the sign in button is pressed. Sends the user back to the sign in screen.
     *
     * @param event The {@link ActionEvent}.
     */
    private void onSignInPress (ActionEvent event) {
        if (this.previousScreen instanceof SignInScreen)
            this.previousScreen.show();
        else {
            // Create a new sign in screen if it wasn't the previous screen.
            new SignInScreen(this.casino, this.stage, this.scene, this).show();
        }
    }

    @Override
    public String getTitle() {
        return "Sign Up";
    }

}

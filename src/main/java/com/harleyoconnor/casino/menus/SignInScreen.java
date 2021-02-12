package com.harleyoconnor.casino.menus;

import com.harleyoconnor.casino.AppConstants;
import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.builders.*;
import com.harleyoconnor.casino.users.User;
import com.harleyoconnor.casino.users.Users;
import com.harleyoconnor.casino.utils.InterfaceUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * @author Harley O'Connor
 */
public final class SignInScreen extends MenuScreen {

    private TextField usernameField;
    private TextField passwordField;
    private Label errorLabel;

    public SignInScreen(Casino casino, Stage stage, Scene scene, StackPane parentView, MenuScreen previousScreen) {
        super(casino, stage, scene, parentView, previousScreen);
    }

    @Override
    protected Pane setupScreen() {
        // Create title label.
        final Label titleLabel = LabelBuilder.create().text("Sign In").title().styleClasses(AppConstants.WHITE_TEXT).wrapText().build();

        // Create fields for username, password, and password confirmation.
        this.usernameField = TextFieldBuilder.createTextField().placeholder("Username").onEnter(this::onSignInPress).build();
        this.passwordField = TextFieldBuilder.createPasswordField().placeholder("Password").onEnter(this::onSignInPress).build();

        // Create error label, for when the user enters details wrong - such as entering an existing username.
        this.errorLabel = LabelBuilder.create().body().styleClasses(AppConstants.WHITE_TEXT).wrapText().build();

        // Create sign up button - this will redirect the user to the sign up screen.
        final Button signUpButton = ButtonBuilder.create().text("Sign Up").onAction(this::onSignUpPress).build();

        // Create sign in button - this will attempt to sign the user in based on the details given.
        final Button signInButton = ButtonBuilder.create().text("Sign In").onAction(this::onSignInPress).build();

        // Create vertical box and add the content to it.
        VBox vBox = VBoxBuilder.create().add(titleLabel, this.usernameField, this.passwordField,
                HBoxBuilder.create().add(signUpButton, InterfaceUtils.createHorizontalSpacer(), signInButton).build(), this.errorLabel)
                .spacing().fixWidth(300).padding(25).centre().build();

        // Create and return horizontal box with vertical box of content in the centre.
        return HBoxBuilder.create().add(vBox).centre().build();
    }

    /**
     * Executes when the sign in button is pressed. Checks that the username and password are valid, that
     * the user exists and the password entered is correct. If those conditions were not met, the
     * <tt>errorLabel</tt> is updated and we return, otherwise we find and log in the user, and open the
     * game menu.
     *
     * @param event The {@link Event}.
     */
    private void onSignInPress (Event event) {
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();

        // Make sure user has entered a valid username and password.
        if (username.length() < 1 || password.length() < 8) {
            this.errorLabel.setText("You must enter a valid username and password.");
            return;
        }

        Optional<User> user = Users.find(username);

        // Make user exists and if it does make sure password was correct.
        if (user.isEmpty() || !user.get().getPasswordHandler().authenticate(password)) {
            this.errorLabel.setText("Username or password was incorrect.");
            return;
        }

        this.casino.setCurrentUser(user.get());
        this.toNewScreen(new GamesMenuScreen(this.casino, this.stage, this.scene, this.parentView, this));
    }

    /**
     * Executes when the sign up button is pressed. Sends the user back to the sign up screen.
     *
     * @param event The {@link Event}.
     */
    private void onSignUpPress (Event event) {
        if (this.previousScreen instanceof SignUpScreen)
            this.toNewScreen(this.previousScreen);
        else {
            // Create the sign up screen if it wasn't the previous screen.
            this.toNewScreen(new SignUpScreen(this.casino, this.stage, this.scene, this.parentView, this));
        }
    }

    @Override
    public String getTitle() {
        return "Sign In";
    }

}

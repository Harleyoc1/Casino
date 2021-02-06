package com.harleyoconnor.casino.menus;

import com.harleyoconnor.casino.AppConstants;
import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.builders.ButtonBuilder;
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
import javafx.scene.layout.HBox;
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
    private TextField confirmPasswordField;
    private Label errorLabel;

    public SignUpScreen(Stage stage, Scene scene, MenuScreen previousScreen) {
        super(stage, scene, previousScreen);
    }

    @Override
    protected Pane setupScreen() {
        Pane layout = new StackPane();

        // Create a vertical box for content with a fixed width of 300.
        VBox vBox = InterfaceUtils.fixWidth(new VBox(5), 300);

        final Label titleLabel = LabelBuilder.createLabel().text("Sign Up").styleClasses(AppConstants.TITLE_CLASS).wrapText().build();

        // Create fields.
        this.usernameField = TextFieldBuilder.createTextField().placeholder("Username").build();
        this.passwordField = TextFieldBuilder.createPasswordField().placeholder("Password").build();
        this.confirmPasswordField = TextFieldBuilder.createPasswordField().placeholder("Confirm Password").build();

        // Create error label, for when the user enters details wrong - such as entering an existing username.
        this.errorLabel = LabelBuilder.createLabel().wrapText().build();

        // Create sign up button - this will attempt to sign the user up based on the entered data.
        Button signUpButton = ButtonBuilder.createButton().text("Sign Up").onAction(this::onSignUpPress).build();

        // Create sign in button - this will redirect the user to the sign in screen.
        Button signInButton = ButtonBuilder.createButton().text("Sign In").onAction(this::onSignInPress).build();

        // Add content to the vertical box.
        InterfaceUtils.centreElementsVertically(InterfaceUtils.addElementsToPane(vBox, titleLabel, this.usernameField, this.passwordField, this.confirmPasswordField,
                InterfaceUtils.addElementsToPane(new HBox(), signInButton, InterfaceUtils.createHorizontalSpacer(), signUpButton),
                this.errorLabel));

        // Set padding for the layout.
        layout.setPadding(new Insets(25));

        // Add vertical box to the layout in the horizontal centre of the screen.
        InterfaceUtils.addElementsToPane(layout, InterfaceUtils.centreElementsHorizontally(InterfaceUtils.addElementsToPane(new HBox(), vBox)));

        return layout;
    }

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
        Casino.getInstance().setCurrentUser(user); // Set new user to current one.
        new GamesMenuScreen(this.stage, this.scene, this).show();
    }

    private void onSignInPress (ActionEvent event) {
        if (this.previousScreen instanceof SignInScreen)
            this.previousScreen.show();
        else {
            // Create a new sign in screen if it wasn't the previous screen.
            new SignInScreen(this.stage, this.scene, this).show();
        }
    }

    @Override
    public String getTitle() {
        return "Sign Up";
    }

}

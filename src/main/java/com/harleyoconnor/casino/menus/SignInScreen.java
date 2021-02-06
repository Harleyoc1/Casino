package com.harleyoconnor.casino.menus;

import com.harleyoconnor.casino.AppConstants;
import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.builders.ButtonBuilder;
import com.harleyoconnor.casino.builders.LabelBuilder;
import com.harleyoconnor.casino.builders.TextFieldBuilder;
import com.harleyoconnor.casino.users.User;
import com.harleyoconnor.casino.users.Users;
import com.harleyoconnor.casino.utils.InterfaceUtils;
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

import java.util.Optional;

/**
 * @author Harley O'Connor
 */
public final class SignInScreen extends MenuScreen {

    private TextField usernameField;
    private TextField passwordField;
    private Label errorLabel;

    public SignInScreen(Stage stage, Scene scene, MenuScreen previousScreen) {
        super(stage, scene, previousScreen);
    }

    @Override
    protected Pane setupScreen() {
        final Pane layout = new StackPane();

        // Create a vertical box for content with a fixed width of 300.
        VBox vBox = InterfaceUtils.fixWidth(new VBox(5), 300);

        final Label titleLabel = LabelBuilder.createLabel().text("Sign In").styleClasses(AppConstants.TITLE_CLASS).wrapText().build();

        // Create fields.
        this.usernameField = TextFieldBuilder.createTextField().placeholder("Username").build();
        this.passwordField = TextFieldBuilder.createPasswordField().placeholder("Password").build();

        // Create error label, for when the user enters details wrong - such as entering an existing username.
        this.errorLabel = LabelBuilder.createLabel().wrapText().build();

        // Create sign up button - this will redirect the user to the sign up screen.
        final Button signUpButton = ButtonBuilder.createButton().text("Sign Up").onAction(this::onSignUpPress).build();

        // Create sign in button - this will attempt to sign the user in based on the details given.
        final Button signInButton = ButtonBuilder.createButton().text("Sign In").onAction(this::onSignInPress).build();

        // Add content to the vertical box.
        InterfaceUtils.centreElementsVertically(InterfaceUtils.addElementsToPane(vBox, titleLabel, this.usernameField, this.passwordField,
                InterfaceUtils.addElementsToPane(new HBox(), signUpButton, InterfaceUtils.createHorizontalSpacer(), signInButton),
                this.errorLabel));

        // Set padding for the layout.
        layout.setPadding(new Insets(25));

        // Add vertical box to the layout in the horizontal centre of the screen.
        InterfaceUtils.addElementsToPane(layout, InterfaceUtils.centreElementsHorizontally(InterfaceUtils.addElementsToPane(new HBox(), vBox)));

        return layout;
    }

    private void onSignInPress (ActionEvent event) {
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

        Casino.getInstance().setCurrentUser(user.get());
        new GamesMenuScreen(this.stage, this.scene, this).show();
    }

    private void onSignUpPress (ActionEvent event) {
        if (this.previousScreen instanceof SignUpScreen)
            this.previousScreen.show();
        else {
            // Create the sign up screen if it wasn't the previous screen.
            new SignUpScreen(this.stage, this.scene, this).show();
        }
    }

    @Override
    public String getTitle() {
        return "Sign In";
    }

}

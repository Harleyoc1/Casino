package com.harleyoconnor.casino.menus;

import com.harleyoconnor.casino.AppConstants;
import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.builders.*;
import com.harleyoconnor.casino.users.User;
import com.harleyoconnor.casino.users.Users;
import com.harleyoconnor.javautilities.FileUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public final class AccountScreen extends MenuScreen {

    private final AccountStageHandler stageHandler;
    private final User user;
    private final TextFieldBuilder<PasswordField> passwordField = TextFieldBuilder.createPasswordField().onEnter(this::onUpdatePress).fixWidth(225);
    private final TextFieldBuilder<PasswordField> confirmPasswordField = TextFieldBuilder.createPasswordField().onEnter(this::onUpdatePress).fixWidth(225);
    private final LabelBuilder<Label> errorLabel = LabelBuilder.create().wrapText().padding(5).fixWidth(225);

    public AccountScreen(Casino casino, AccountStageHandler stageHandler, Stage stage, Scene scene, StackPane parentView) {
        super(casino, stage, scene, parentView, null, false);

        this.stageHandler = stageHandler;
        scene.getStylesheets().add(AppConstants.FILE_PREFIX + FileUtils.getFile(AppConstants.ACCOUNT_STYLESHEET_PATH).getPath());
        this.user = casino.getCurrentUser();
    }

    @Override
    protected Pane setupScreen() {
        return HBoxBuilder.create().add(VBoxBuilder.create().add(LabelBuilder.create().text("New Password").body().build(), this.passwordField.build(),
                LabelBuilder.create().text("Confirm New Password").padding(10, 0, 0, 0).build(), this.confirmPasswordField.build(),
                this.errorLabel.build(), HBoxBuilder.create().add(ButtonBuilder.create().text("Sign Out").onAction(this::onSignOut).styleClasses("black-border").fixWidth(110).build(),
                        ButtonBuilder.create().text("Update").styleClasses("black-border").fixWidth(110).onAction(this::onUpdatePress).build()).spacing().build()).centre().build()).centre().build();
    }

    /**
     * Executes on update press, checking the passwords match and are 8 characters or more, and changing
     * it if these conditions were met.
     *
     * @param event The {@link Event}.
     */
    private void onUpdatePress (Event event) {
        String newPassword = this.passwordField.build().getText();
        String newConfirmPassword = this.confirmPasswordField.build().getText();

        // Ensure the passwords matched.
        if (!newPassword.equals(newConfirmPassword)) {
            this.errorLabel.text("Passwords did not match.");
            return;
        }

        // Ensure the password is secure enough - 8 characters is the generally accepted value.
        if (newPassword.length() < 8) {
            this.errorLabel.text("Passwords must have at least 8 characters.");
            return;
        }

        // Set and hash the new password.
        this.user.getPasswordHandler().setPassword(newPassword).hash();
        // Close the stage.
        this.stageHandler.close();
    }

    /**
     * Executes on sign out press, signing out the user and sending them back to the sign in screen.
     *
     * @param event The {@link ActionEvent}.
     */
    private void onSignOut (ActionEvent event) {
        // A "null" user means that there is not a user signed in.
        this.casino.setCurrentUser(null);
        // Close this window.
        this.stageHandler.close();

        MenuScreen otherScreen = this.stageHandler.getOtherOpenScreen();

        // Transitions the other screen back to the sign in screen.
        otherScreen.toNewScreen(new SignInScreen(this.casino, otherScreen.stage, otherScreen.scene, otherScreen.parentView, otherScreen));

        // Tells the Users Json we have updated a user so it can write the changes.
        Users.updated();
    }

    @Override
    public void show() {
        this.layout = this.setupScreen();
        super.show();
    }

    @Override
    public Pane addViewStyleClass(Pane pane) {
        return pane; // Return the pane so the default stylesheet is not added.
    }

    @Override
    public void setTitle() {
        this.stage.setTitle(this.getTitle());
    }

    @Override
    public String getTitle() {
        return "Account";
    }

}

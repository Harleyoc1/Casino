package com.harleyoconnor.casino.builders;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * A helper class that helps easily construct {@link TextField} objects.
 *
 * @author Harley O'Connor
 */
public final class TextFieldBuilder<T extends TextField> extends RegionBuilder<T, TextFieldBuilder<T>> {

    public TextFieldBuilder(T node) {
        super(node);
    }

    /**
     * Executes the given {@link EventHandler} when the enter key is pressed.
     *
     * @param eventHandler The {@link EventHandler} object to call.
     * @return This {@link TextFieldBuilder} object.
     */
    public TextFieldBuilder<T> onEnter (EventHandler<Event> eventHandler) {
        this.node.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() != KeyCode.ENTER) return;
            eventHandler.handle(event);
        });
        return this;
    }

    public TextFieldBuilder<T> placeholder(String text) {
        this.node.setPromptText(text);
        return this;
    }

    public TextFieldBuilder<T> focus() {
        this.node.requestFocus();
        return this;
    }

    public static TextFieldBuilder<TextField> createTextField () {
        return new TextFieldBuilder<>(new TextField());
    }

    public static TextFieldBuilder<PasswordField> createPasswordField () {
        return new TextFieldBuilder<>(new PasswordField());
    }

}

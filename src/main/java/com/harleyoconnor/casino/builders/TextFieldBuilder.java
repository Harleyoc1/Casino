package com.harleyoconnor.casino.builders;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * @author Harley O'Connor
 */
public final class TextFieldBuilder<T extends TextField> {

    private final T textField;

    public TextFieldBuilder(T textField) {
        this.textField = textField;
    }

    public TextFieldBuilder<T> placeholder(String text) {
        this.textField.setPromptText(text);
        return this;
    }

    public T build () {
        return textField;
    }

    public static TextFieldBuilder<TextField> createTextField () {
        return new TextFieldBuilder<>(new TextField());
    }

    public static TextFieldBuilder<PasswordField> createPasswordField () {
        return new TextFieldBuilder<>(new PasswordField());
    }

}

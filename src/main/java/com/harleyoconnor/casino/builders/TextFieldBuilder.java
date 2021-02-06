package com.harleyoconnor.casino.builders;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * @author Harley O'Connor
 */
public final class TextFieldBuilder<T extends TextField> extends NodeBuilder<T, TextFieldBuilder<T>> {

    public TextFieldBuilder(T node) {
        super(node);
    }

    public TextFieldBuilder<T> placeholder(String text) {
        this.node.setPromptText(text);
        return this;
    }

    public static TextFieldBuilder<TextField> createTextField () {
        return new TextFieldBuilder<>(new TextField());
    }

    public static TextFieldBuilder<PasswordField> createPasswordField () {
        return new TextFieldBuilder<>(new PasswordField());
    }

}

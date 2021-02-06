package com.harleyoconnor.casino.builders;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * @author Harley O'Connor
 */
public final class ButtonBuilder<T extends Button> implements NodeBuilder<T> {

    private final T button;

    public ButtonBuilder(T button) {
        this.button = button;
    }

    public ButtonBuilder<T> text(String text) {
        this.button.setText(text);
        return this;
    }

    public ButtonBuilder<T> onAction(EventHandler<ActionEvent> eventHandler) {
        this.button.setOnAction(eventHandler);
        return this;
    }

    @Override
    public T build() {
        return this.button;
    }

    public static ButtonBuilder<Button> createButton () {
        return new ButtonBuilder<>(new Button());
    }

}

package com.harleyoconnor.casino.builders;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * A helper class that helps easily construct {@link Button} objects.
 *
 * @author Harley O'Connor
 */
public final class ButtonBuilder<T extends Button> extends RegionBuilder<T, ButtonBuilder<T>> {

    public ButtonBuilder(T node) {
        super(node);
    }

    public ButtonBuilder<T> text(String text) {
        this.node.setText(text);
        return this;
    }

    public ButtonBuilder<T> onAction(EventHandler<ActionEvent> eventHandler) {
        this.node.setOnAction(eventHandler);
        return this;
    }

    public static ButtonBuilder<Button> createButton () {
        return new ButtonBuilder<>(new Button());
    }

}

package com.harleyoconnor.casino.builders;

import javafx.scene.control.Label;

/**
 * A helper class that helps easily construct {@link Label} objects.
 *
 * @author Harley O'Connor
 */
public final class LabelBuilder<T extends Label> extends NodeBuilder<T, LabelBuilder<T>> {

    public LabelBuilder(T label) {
        super(label);
    }

    public LabelBuilder<T> text(String text) {
        this.node.setText(text);
        return this;
    }

    public LabelBuilder<T> wrapText () {
        this.node.setWrapText(true);
        return this;
    }

    public static LabelBuilder<Label> createLabel () {
        return new LabelBuilder<>(new Label());
    }

}

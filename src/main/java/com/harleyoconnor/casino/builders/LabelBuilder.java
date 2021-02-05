package com.harleyoconnor.casino.builders;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * @author Harley O'Connor
 */
public final class LabelBuilder<T extends Label> {

    private final T label;

    public LabelBuilder(T label) {
        this.label = label;
    }

    public LabelBuilder<T> withText(String text) {
        this.label.setText(text);
        return this;
    }

    public LabelBuilder<T> wrapText () {
        this.label.setWrapText(true);
        return this;
    }

    public T build() {
        return label;
    }

    public static LabelBuilder<Label> createLabel () {
        return new LabelBuilder<>(new Label());
    }

}

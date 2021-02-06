package com.harleyoconnor.casino.builders;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 * @author Harley O'Connor
 */
public final class LabelBuilder<T extends Label> implements NodeBuilder<T> {

    private final T label;

    public LabelBuilder(T label) {
        this.label = label;
    }

    public LabelBuilder<T> text(String text) {
        this.label.setText(text);
        return this;
    }

    public LabelBuilder<T> font(Font font) {
        this.label.setFont(font);
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

package com.harleyoconnor.casino.builders;

import javafx.scene.layout.StackPane;

/**
 * @author Harley O'Connor
 */
public final class StackPaneBuilder<T extends StackPane> extends PaneBuilder<T, StackPaneBuilder<T>> {

    public StackPaneBuilder(T stackPane) {
        super(stackPane);
    }

    public static StackPaneBuilder<StackPane> create () {
        return new StackPaneBuilder<>(new StackPane());
    }

    public static StackPaneBuilder<StackPane> edit(StackPane stackPane) {
        return new StackPaneBuilder<>(stackPane);
    }

}

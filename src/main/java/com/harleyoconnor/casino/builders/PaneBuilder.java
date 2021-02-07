package com.harleyoconnor.casino.builders;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * @author Harley O'Connor
 */
@SuppressWarnings("unchecked") // These warnings are invalid considering this is an abstract class, so ignore them.
public abstract class PaneBuilder<T extends Pane, V extends PaneBuilder<T, V>> extends RegionBuilder<T, V> {

    public PaneBuilder(T pane) {
        super(pane);
    }

    /**
     * Adds the given {@link Node} to the {@link Pane}.
     *
     * @param node The {@link Node} to add.
     * @return This {@link Pane} builder.
     */
    public V add (Node node) {
        this.node.getChildren().add(node);
        return (V) this;
    }

    /**
     * Adds the given {@link Node} objects to the {@link Pane}.
     *
     * @param node The {@link Node} objects to add.
     * @return This {@link Pane} builder.
     */
    public V add (Node... node) {
        this.node.getChildren().addAll(node);
        return (V) this;
    }

}

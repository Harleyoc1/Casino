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
     * Adds the given {@link Node} objects to the {@link Pane}.
     *
     * @param node The {@link Node} objects to add.
     * @return This {@link Pane} builder.
     */
    public V add (Node... node) {
        this.node.getChildren().addAll(node);
        return (V) this;
    }

    /**
     * Adds the given {@link Node} object to the {@link Pane} at the given index.
     *
     * @param node The {@link Node} object to add.
     * @param index The index to insert to.
     * @return This {@link Pane} builder.
     */
    public V insert (Node node, int index) {
        this.node.getChildren().add(index, node);
        return (V) this;
    }

}

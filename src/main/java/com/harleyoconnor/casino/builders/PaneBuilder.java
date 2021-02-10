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
     * @param nodes The {@link Node} objects to add.
     * @return This {@link Pane} builder.
     */
    public V add (Node... nodes) {
        this.node.getChildren().addAll(nodes);
        return (V) this;
    }

    /**
     * Adds each given {@link Node} object to the {@link Pane}, if it's not already present.
     *
     * @param nodes The {@link Node} objects to add.
     * @return This {@link Pane} builder.
     */
    public V addIfNotPresent (Node... nodes) {
        for (Node node : nodes) {
            if (!this.node.getChildren().contains(node))
                this.add(node);
        }
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

    /**
     * Adds the given {@link Node} object to the {@link Pane} at the given index, if not already present.
     *
     * @param node The {@link Node} object to add.
     * @param index The index to insert to.
     * @return This {@link Pane} builder.
     */
    public V insertIfNotPresent (Node node, int index) {
        return this.node.getChildren().contains(node) ? (V) this : this.insert(node, index);
    }

    /**
     * Removes the given {@link Node} objects from the {@link Pane}.
     *
     * @param nodes The {@link Node} objects to remove.
     * @return This {@link Pane} builder.
     */
    public V remove (Node... nodes) {
        this.node.getChildren().removeAll(nodes);
        return (V) this;
    }

}

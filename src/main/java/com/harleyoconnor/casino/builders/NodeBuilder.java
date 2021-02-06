package com.harleyoconnor.casino.builders;

import javafx.scene.Node;

/**
 * An abstract class for a simple JavaFX node builder.
 *
 * @author Harley O'Connor
 */
@SuppressWarnings("unchecked") // These warnings are invalid considering this is an abstract class, so ignore them.
public abstract class NodeBuilder<T extends Node, V extends NodeBuilder<T, V>> {

    protected final T node;

    public NodeBuilder(T node) {
        this.node = node;
    }

    /**
     * Adds style classes to node.
     *
     * @param styleClassIds The style classes to add.
     * @return This node builder.
     */
    public V styleClasses(String... styleClassIds) {
        this.node.getStyleClass().addAll(styleClassIds);
        return (V) this;
    }

    /**
     * @return The node that's been configured.
     */
    public T build () {
        return this.node;
    }

}

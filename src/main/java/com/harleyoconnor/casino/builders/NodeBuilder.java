package com.harleyoconnor.casino.builders;

import javafx.scene.layout.Region;
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
     * Fixes width of node by setting max and min width to width given, if node is a {@link Region}.
     *
     * @param width The width to fix.
     * @return This node builder.
     */
    public V fixWidth (int width) {
        if (this.node instanceof Region) {
            Region region = (Region) this.node;
            region.setMinWidth(width);
            region.setMaxWidth(width);
        }
        return (V) this;
    }

    /**
     * Fixes height of node to height given, if node is a {@link Region}.
     *
     * @param height The height to fix.
     * @return This node builder.
     */
    public V fixHeight (int height) {
        if (this.node instanceof Region) {
            Region region = (Region) this.node;
            region.setMinHeight(height);
            region.setMaxHeight(height);
        }
        return (V) this;
    }

    /**
     * Fixes both width and height of node to given value, if node is a {@link Region}.
     *
     * @param widthAndHeight The height and width to fix.
     * @return This node builder.
     */
    public V fixWidthHeight (int widthAndHeight) {
        return this.fixWidthHeight(widthAndHeight, widthAndHeight);
    }

    /**
     * Fixes both width and height of node to given width and height, if node is a {@link Region}.
     *
     * @param width The width to fix.
     * @param height The height to fix.
     * @return This node builder.
     */
    public V fixWidthHeight (int width, int height) {
        this.fixWidth(width);
        return this.fixHeight(height);
    }

    /**
     * @return The node that's been configured.
     */
    public T build () {
        return this.node;
    }

}

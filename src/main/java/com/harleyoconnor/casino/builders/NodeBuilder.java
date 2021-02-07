package com.harleyoconnor.casino.builders;

import com.harleyoconnor.casino.AppConstants;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import javafx.scene.Node;

/**
 * An abstract class for a simple JavaFX node builder.
 *
 * @author Harley O'Connor
 */
@SuppressWarnings("unchecked") // These warnings are invalid considering this is an abstract class, so ignore them.
public abstract class NodeBuilder<T extends Node, V extends NodeBuilder<T, V>> {

    // TODO: Make extension "RegionBuilder" to fix the mess of checking instanceof for some methods.

    protected final T node;

    public NodeBuilder(T node) {
        this.node = node;
    }

    /**
     * Adds style classes to the {@link Node}.
     *
     * @param styleClassIds The style classes to add.
     * @return This {@link Node} builder.
     */
    public V styleClasses(String... styleClassIds) {
        this.node.getStyleClass().addAll(styleClassIds);
        return (V) this;
    }

    /**
     * Adds title style class to the {@link Node}.
     *
     * @return This {@link Node} builder.
     */
    public V title() {
        return this.styleClasses(AppConstants.TITLE_CLASS);
    }

    /**
     * Adds body style class to the {@link Node}.
     *
     * @return This {@link Node} builder.
     */
    public V body() {
        return this.styleClasses(AppConstants.BODY_CLASS);
    }

    /**
     * @return The {@link Node} that's been configured.
     */
    public T build () {
        return this.node;
    }

}

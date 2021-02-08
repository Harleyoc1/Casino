package com.harleyoconnor.casino.builders;

import com.harleyoconnor.casino.AppConstants;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;

import javax.annotation.Nonnull;

/**
 * An abstract class for a simple JavaFX node builder.
 *
 * @author Harley O'Connor
 */
@SuppressWarnings("unchecked") // These warnings are invalid considering this is an abstract class, so ignore them.
public abstract class NodeBuilder<T extends Node, V extends NodeBuilder<T, V>> implements Builder<T> {

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
     * Translates the {@link Node} object's x-axis by the given amount.
     *
     * @param amount The amount to translate it.
     * @return This {@link Node} builder.
     */
    public V translateX (double amount) {
        return this.translate('X', amount);
    }

    /**
     * Translates the {@link Node} object's y-axis by the given amount.
     *
     * @param amount The amount to translate it.
     * @return This {@link Node} builder.
     */
    public V translateY (double amount) {
        return this.translate('Y', amount);
    }

    /**
     * Translates the {@link Node} object's z-axis by the given amount.
     *
     * @param amount The amount to translate it.
     * @return This {@link Node} builder.
     */
    public V translateZ (double amount) {
        return this.translate('Z', amount);
    }

    /**
     * Translates the {@link Node} object's given axis by the given amount.
     *
     * @param xyz The axis translate.
     * @param amount The amount to translate it.
     * @return This {@link Node} builder.
     */
    private V translate(char xyz, double amount) {
        this.getTranslate(xyz).set(amount);
        return (V) this;
    }

    /**
     * Gets the {@link DoubleProperty} for the given axis.
     *
     * @param xyz The axis to get the {@link DoubleProperty} for.
     * @return The {@link DoubleProperty} for the given axis.
     */
    private DoubleProperty getTranslate (char xyz) {
        return switch (xyz) {
            case 'X' -> this.node.translateXProperty();
            case 'Y' -> this.node.translateYProperty();
            default -> this.node.translateZProperty();
        };
    }

    /**
     * @return The {@link Node} that's been configured.
     */
    @Nonnull
    @Override
    public T build () {
        return this.node;
    }

}

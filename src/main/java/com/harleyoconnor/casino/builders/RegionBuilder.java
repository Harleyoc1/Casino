package com.harleyoconnor.casino.builders;

import javafx.geometry.Insets;
import javafx.scene.layout.Region;

/**
 * @author Harley O'Connor
 */
@SuppressWarnings("unchecked") // These warnings are invalid considering this is an abstract class, so ignore them.
public abstract class RegionBuilder<T extends Region, V extends RegionBuilder<T, V>> extends NodeBuilder<T, V> {

    public RegionBuilder(T region) {
        super(region);
    }

    /**
     * Applies the default amount of padding (15) to each side of the {@link Region}.
     *
     * @return This {@link Region} builder.
     */
    public V padding() {
        return this.padding(15);
    }

    /**
     * Applies given amount of padding to each side of the {@link Region}.
     *
     * @param topRightBottomLeft The amount of padding to apply to the top, right, bottom, and left.
     * @return This {@link Region} builder.
     */
    public V padding(int topRightBottomLeft) {
        return this.padding(topRightBottomLeft, topRightBottomLeft, topRightBottomLeft, topRightBottomLeft);
    }

    /**
     * Applies given amount of padding for each side to each side of the {@link Region}.
     *
     * @param top The amount of padding to apply to the top.
     * @param right The amount of padding to apply to the right.
     * @param bottom The amount of padding to apply to the bottom.
     * @param left The amount of padding to apply to the left.
     * @return This {@link Region} builder.
     */
    public V padding(int top, int right, int bottom, int left) {
        this.node.setPadding(new Insets(top, right, bottom, left));
        return (V) this;
    }

    /**
     * Sets the minimum width of the {@link Region} to the given width.
     *
     * @param width The width to set as a minimum.
     * @return This {@link Region} builder.
     */
    public V minWidth (int width) {
        this.node.setMinWidth(width);
        return (V) this;
    }

    /**
     * Sets the minimum height of the {@link Region} to the given height.
     *
     * @param height The height to set as a minimum.
     * @return This {@link Region} builder.
     */
    public V minHeight (int height) {
        this.node.setMinHeight(height);
        return (V) this;
    }

    /**
     * Sets both maximum width and height of the {@link Region} to the given value.
     *
     * @param widthHeight The height and width to set as a minimum.
     * @return This {@link Region} builder.
     */
    public V minWidthHeight (int widthHeight) {
        this.maxWidth(widthHeight);
        return this.maxHeight(widthHeight);
    }

    /**
     * Sets both minimum width and height of the {@link Region} to the minimum width and height given.
     *
     * @param width The width to set as a minimum.
     * @param height The height to set as a minimum.
     * @return This {@link Region} builder.
     */
    public V minWidthHeight (int width, int height) {
        this.maxWidth(width);
        return this.maxHeight(height);
    }

    /**
     * Sets the maximum width of the {@link Region} to the given width.
     *
     * @param width The width to set as a maximum.
     * @return This {@link Region} builder.
     */
    public V maxWidth (int width) {
        this.node.setMaxWidth(width);
        return (V) this;
    }

    /**
     * Sets the maximum height of the {@link Region} to the given height.
     *
     * @param height The height to set as a maximum.
     * @return This {@link Region} builder.
     */
    public V maxHeight (int height) {
        this.node.setMaxHeight(height);
        return (V) this;
    }

    /**
     * Sets both maximum width and height of the {@link Region} to the given value.
     *
     * @param widthHeight The height and width to set as a maximum.
     * @return This {@link Region} builder.
     */
    public V maxWidthHeight (int widthHeight) {
        this.maxWidth(widthHeight);
        return this.maxHeight(widthHeight);
    }

    /**
     * Sets both maximum width and height of the {@link Region} to the maximum width and height given.
     *
     * @param width The width to set as a maximum.
     * @param height The height to set as a maximum.
     * @return This {@link Region} builder.
     */
    public V maxWidthHeight (int width, int height) {
        this.maxWidth(width);
        return this.maxHeight(height);
    }

    /**
     * Fixes width of the {@link Region} by setting max and min width to width given.
     *
     * @param width The width to fix.
     * @return This {@link Region} builder.
     */
    public V fixWidth (int width) {
        this.node.setMinWidth(width);
        this.node.setMaxWidth(width);
        return (V) this;
    }

    /**
     * Fixes height of {@link Region} to height given.
     *
     * @param height The height to fix.
     * @return This {@link Region} builder.
     */
    public V fixHeight (int height) {
        this.node.setMinHeight(height);
        this.node.setMaxHeight(height);
        return (V) this;
    }

    /**
     * Fixes both width and height of the {@link Region} to given value.
     *
     * @param widthAndHeight The height and width to fix.
     * @return This {@link Region} builder.
     */
    public V fixWidthHeight (int widthAndHeight) {
        return this.fixWidthHeight(widthAndHeight, widthAndHeight);
    }

    /**
     * Fixes both width and height of the {@link Region} to given width and height.
     *
     * @param width The width to fix.
     * @param height The height to fix.
     * @return This {@link Region} builder.
     */
    public V fixWidthHeight (int width, int height) {
        this.fixWidth(width);
        return this.fixHeight(height);
    }

}

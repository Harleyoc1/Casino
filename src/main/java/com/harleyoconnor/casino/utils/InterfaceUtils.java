package com.harleyoconnor.casino.utils;

import com.harleyoconnor.casino.AppConstants;
import com.harleyoconnor.casino.builders.HBoxBuilder;
import com.harleyoconnor.casino.builders.VBoxBuilder;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * @author Harley O'Connor
 */
public final class InterfaceUtils {

    public static Region createHorizontalSpacer() {
        final Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    public static Region createVerticalSpacer() {
        final Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    /**
     * Puts the given {@link Node} objects into a {@link HBox}, centring them horizontally and
     * applying the default spacing and padding.
     *
     * @param nodes The {@link Node} objects.
     * @return The {@link HBox} created.
     */
    public static HBox centreHorizontally(Node... nodes) {
        return centreHorizontally(AppConstants.DEFAULT_SPACING, nodes);
    }

    /**
     * Puts the given {@link Node} objects into a {@link VBox}, centring them vertically and
     * applying the default spacing and padding.
     *
     * @param nodes The {@link Node} objects.
     * @return The {@link VBox} created.
     */
    public static VBox centreVertically(Node... nodes) {
        return centreVertically(AppConstants.DEFAULT_SPACING, nodes);
    }

    /**
     * Puts the given {@link Node} objects into a {@link HBox}, centring them horizontally and
     * applying the given spacing and default padding.
     *
     * @param spacing The spacing to apply to the {@link HBox}.
     * @param nodes The {@link Node} objects.
     * @return The {@link HBox} created.
     */
    public static HBox centreHorizontally(int spacing, Node... nodes) {
        return centreHorizontally(AppConstants.DEFAULT_PADDING, spacing, nodes);
    }

    /**
     * Puts the given {@link Node} objects into a {@link VBox}, centring them vertically and
     * applying the given spacing and default padding.
     *
     * @param spacing The spacing to apply to the {@link VBox}.
     * @param nodes The {@link Node} objects.
     * @return The {@link VBox} created.
     */
    public static VBox centreVertically(int spacing, Node... nodes) {
        return centreVertically(AppConstants.DEFAULT_PADDING, spacing, nodes);
    }

    /**
     * Puts the given {@link Node} objects into a {@link HBox}, centring them horizontally and
     * applying the given spacing and padding.
     *
     * @param padding The padding to apply to the {@link HBox}.
     * @param spacing The spacing to apply to the {@link HBox}.
     * @param nodes The {@link Node} objects.
     * @return The {@link HBox} created.
     */
    public static HBox centreHorizontally(int padding, int spacing, Node... nodes) {
        return HBoxBuilder.create().add(nodes).padding(padding).spacing(spacing).centre().build();
    }

    /**
     * Puts the given {@link Node} objects into a {@link VBox}, centring them vertically and
     * applying the given spacing and padding.
     *
     * @param padding The padding to apply to the {@link VBox}.
     * @param spacing The spacing to apply to the {@link VBox}.
     * @param nodes The {@link Node} objects.
     * @return The {@link VBox} created.
     */
    public static VBox centreVertically(int padding, int spacing, Node... nodes) {
        return VBoxBuilder.create().add(nodes).padding(padding).spacing(spacing).centre().build();
    }

}

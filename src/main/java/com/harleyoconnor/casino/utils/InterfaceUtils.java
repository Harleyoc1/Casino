package com.harleyoconnor.casino.utils;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * @author Harley O'Connor
 */
public final class InterfaceUtils {

    public static ImageView createImageView (final String imagePath, final int height, final int width) {
        final ImageView imageView = new ImageView(new Image(imagePath));

        imageView.setFitHeight(height);
        imageView.setFitWidth(width);

        return imageView;
    }

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

    public static <T extends HBox> T centreElementsHorizontally (final T hBox) {
        hBox.getChildren().add(0, createHorizontalSpacer());
        hBox.getChildren().add(createHorizontalSpacer());
        return hBox;
    }

    public static <T extends VBox> T centreElementsVertically (final T vBox) {
        vBox.getChildren().add(0, createVerticalSpacer());
        vBox.getChildren().add(createVerticalSpacer());
        return vBox;
    }

    public static <T extends Pane> T addElementsToPane (final T pane, final Node... nodes) {
        pane.getChildren().addAll(nodes);
        return pane;
    }

    public static <T extends Region> T fixWidth (final T region, final int width) {
        region.setMinWidth(width);
        region.setMaxWidth(width);
        return region;
    }

}

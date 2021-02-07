package com.harleyoconnor.casino.builders;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Harley O'Connor
 */
public final class ImageViewBuilder<T extends ImageView> extends NodeBuilder<T, ImageViewBuilder<T>> {

    public ImageViewBuilder(T imageView) {
        super(imageView);
    }

    public ImageViewBuilder<T> image(Image image) {
        this.node.setImage(image);
        return this;
    }

    public ImageViewBuilder<T> width(int width) {
        this.node.setFitWidth(width);
        return this;
    }

    public ImageViewBuilder<T> height(int height) {
        this.node.setFitHeight(height);
        return this;
    }

    public ImageViewBuilder<T> preserveRatio() {
        this.node.setPreserveRatio(true);
        return this;
    }

    public static ImageViewBuilder<ImageView> create() {
        return new ImageViewBuilder<>(new ImageView());
    }

}

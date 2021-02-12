package com.harleyoconnor.casino.animations;

import com.harleyoconnor.casino.builders.TimelineBuilder;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.layout.StackPane;

/**
 * Class to handle a flipping animation, used by cards.
 *
 * Credit where credit is due, this is largely based off of
 * <a href="https://stackoverflow.com/a/22541421/6876001">this</a>.
 *
 * @author Harley O'Connor
 */
public class FlipAnimation<T extends Node> implements Animation {

    private static final double PI = Math.PI;
    private static final double HALF_PI = PI / 2;

    private final T frontNode;
    private final T backNode;
    private final StackPane view;
    private final int duration;

    private final Timeline animation;

    private SimpleDoubleProperty angle = new SimpleDoubleProperty(HALF_PI);
    private final PerspectiveTransform transform = new PerspectiveTransform();
    private final SimpleBooleanProperty flippedProperty = new SimpleBooleanProperty(false);

    public FlipAnimation(T frontNode, T backNode, StackPane view, int duration) {
        this.frontNode = this.setupNode(frontNode, this.flippedProperty);
        this.backNode = this.setupNode(backNode, this.flippedProperty.not());
        this.createAngleProperty();
        this.view = this.configureView(view);
        this.duration = duration;
        this.animation = this.createAnimation();
    }

    /**
     * Sets up node by applying the transform effect and the flipped property.
     *
     * @param node The {@link Node} to apply the effect to.
     * @param flippedProperty The flipped boolean property.
     * @param <V> Anything that extends {@link Boolean}, as flipped property must hold a boolean value.
     * @return The {@link Node}.
     */
    private <V extends Boolean> T setupNode (T node, ObservableValue<V> flippedProperty) {
        node.setEffect(this.transform); //Apply the transform effect.
        node.visibleProperty().bind(flippedProperty); // Bind the flipped property given to the visible property.
        return node;
    }

    /**
     * @return The timeline object, populated with necessary {@link KeyFrame} objects.
     */
    private Timeline createAnimation() {
        return TimelineBuilder.create().keyFrame(0, this.angle, HALF_PI)
                .keyFrame(this.duration / 2, this.angle, 0, Interpolator.EASE_IN)
                .keyFrame(this.duration / 2, event -> this.flippedProperty.set(this.flippedProperty.not().get()))
                .keyFrame(this.duration / 2, this.angle, PI)
                .keyFrame(this.duration, this.angle, HALF_PI, Interpolator.EASE_OUT).build();
    }

    /**
     * Configures a {@link StackPane} that will hold both sides of the {@link Node}.
     *
     * @param pane The {@link StackPane} to configure.
     * @return The configured {@link StackPane}.
     */
    public StackPane configureView(StackPane pane) {
        // Configures the pane to recalculate the transformation when its size changes.
        pane.widthProperty().addListener((observableValue, oldValue, newValue) -> this.recalculateTransformation(this.angle.doubleValue()));
        pane.heightProperty().addListener((observableValue, oldValue, newValue) -> this.recalculateTransformation(this.angle.doubleValue()));

        return pane;
    }

    /**
     * Creates the angle property and sets it to recalculate the transformation as it changes.
     */
    private void createAngleProperty() {
        this.angle = new SimpleDoubleProperty(HALF_PI); // Create the property.

        // Add listener to recalculate translation when it changes.
        this.angle.addListener((obsValue, oldValue, newValue) -> this.recalculateTransformation(newValue.doubleValue()));
    }

    /**
     * Flips the view to the other {@link Node}.
     * @return This {@link Animation} object.
     */
    @Override
    public Animation play() {
        this.animation.setRate(this.flippedProperty.get() ? 10 : -10);
        this.animation.play();
        return this;
    }

    @Override
    public Animation stop() {
        this.animation.stop();
        return this;
    }

    @Override
    public Animation setOnFinish(EventHandler<ActionEvent> eventHandler) {
        this.animation.setOnFinished(eventHandler);
        return this;
    }

    /**
     * Performs complicated calculations on the given flip angle to recalculate the transformation.
     *
     * @param angle The flip angle.
     */
    private void recalculateTransformation(final double angle) {
        final double insetsTop = this.view.getInsets().getTop() * 2;
        final double insetsLeft = this.view.getInsets().getLeft() * 2;

        final double radius = this.view.widthProperty().subtract(insetsLeft).divide(2).doubleValue();
        final double height = this.view.heightProperty().subtract(insetsTop).doubleValue();
        final double back = height / 10;

        double value = radius - Math.sin(angle) * radius;
        double anotherValue = radius + Math.sin(angle) * radius;

        this.transform.setUlx(value);
        this.transform.setUly(0 - Math.cos(angle) * back);
        this.transform.setUrx(anotherValue);
        this.transform.setUry(0 + Math.cos(angle) * back);
        this.transform.setLrx(anotherValue);
        this.transform.setLry(height - Math.cos(angle) * back);
        this.transform.setLlx(value);
        this.transform.setLly(height + Math.cos(angle) * back);
    }

}

package com.harleyoconnor.casino.animations;

import com.harleyoconnor.casino.builders.TimelineBuilder;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

/**
 * Animation for handling slides across the screen.
 *
 * @author Harley O'Connor
 */
public class SlideAnimation<T extends Node> implements Animation {

    private final T node;
    private final TranslateAxis axis;
    private final Timeline animation;
    private final double endValue;
    private final int duration;
    private final Interpolator interpolator;

    public SlideAnimation(T node, TranslateAxis axis, double endValue, int duration) {
        this(node, axis, endValue, duration, Interpolator.LINEAR);
    }

    public SlideAnimation(T node, TranslateAxis axis, double endValue, int duration, Interpolator interpolator) {
        this.node = node;
        this.axis = axis;
        this.endValue = endValue;
        this.duration = duration;
        this.interpolator = interpolator;
        this.animation = this.createAnimation();
    }

    /**
     * Creates the animation {@link Timeline} object, setting the duration, axis, end value, and interpolator.
     *
     * @return The {@link Timeline} object created.
     */
    private Timeline createAnimation () {
        return TimelineBuilder.create().keyFrame(this.duration, this.axis.getTranslateProperty(this.node), this.endValue, this.interpolator).build();
    }

    @Override
    public Animation play() {
        this.animation.play();
        return this;
    }

    @Override
    public Animation setOnFinish(EventHandler<ActionEvent> eventHandler) {
        this.animation.setOnFinished(eventHandler);
        return this;
    }

    /**
     * Holds the axis for which the slide (translation) will happen.
     */
    public enum TranslateAxis {
        X, Y, Z;

        /**
         * Gets the {@link DoubleProperty} for the translation for the current Axis object.
         *
         * @param node The {@link Node} object.
         * @return The {@link DoubleProperty} for the translation for the current Axis object.
         */
        private DoubleProperty getTranslateProperty (Node node) {
            return switch (this) {
                case X -> node.translateXProperty();
                case Y -> node.translateYProperty();
                case Z -> node.translateZProperty();
            };
        }
    }

}

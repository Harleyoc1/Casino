package com.harleyoconnor.casino.builders;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.WritableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import javax.annotation.Nonnull;

/**
 * @author Harley O'Connor
 */
public final class TimelineBuilder<T extends Timeline> implements Builder<T> {

    private final T timeline;

    public TimelineBuilder(T timeline) {
        this.timeline = timeline;
    }

    public <V> TimelineBuilder<T> keyFrame (int millis, WritableValue<V> value, V endValue) {
        return this.keyFrame(millis, value, endValue, Interpolator.LINEAR);
    }

    public <V> TimelineBuilder<T> keyFrame (int millis, WritableValue<V> value, V endValue, Interpolator interpolator) {
        return this.keyFrame(Duration.millis(millis), value, endValue, interpolator);
    }

    public <V> TimelineBuilder<T> keyFrame (Duration duration, WritableValue<V> value, V endValue, Interpolator interpolator) {
        this.timeline.getKeyFrames().add(new KeyFrame(duration, new KeyValue(value, endValue, interpolator)));
        return this;
    }

    public TimelineBuilder<T> keyFrame (int millis, EventHandler<ActionEvent> eventHandler) {
        return this.keyFrame(Duration.millis(millis), eventHandler);
    }

    public TimelineBuilder<T> keyFrame (Duration duration, EventHandler<ActionEvent> eventHandler) {
        this.timeline.getKeyFrames().add(new KeyFrame(duration, eventHandler));
        return this;
    }

    public TimelineBuilder<T> keyFrame (KeyFrame keyFrame) {
        this.timeline.getKeyFrames().add(keyFrame);
        return this;
    }

    public TimelineBuilder<T> onFinished (EventHandler<ActionEvent> eventHandler) {
        this.timeline.setOnFinished(eventHandler);
        return this;
    }

    @Nonnull
    @Override
    public T build() {
        return this.timeline;
    }

    public static TimelineBuilder<Timeline> create () {
        return new TimelineBuilder<>(new Timeline());
    }

}

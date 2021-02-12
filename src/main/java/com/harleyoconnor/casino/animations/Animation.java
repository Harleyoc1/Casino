package com.harleyoconnor.casino.animations;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * @author Harley O'Connor
 */
public interface Animation {

    /**
     * Plays the animation.
     * @return This {@link Animation} object.
     */
    Animation play ();

    /**
     * Stops the animation.
     * @return This {@link Animation} object.
     */
    Animation stop ();

    /**
     * Sets an event handler for when the animation has finished.
     *
     * @param eventHandler The event handler to register when finished.
     * @return This {@link Animation} object.
     */
    Animation setOnFinish (EventHandler<ActionEvent> eventHandler);

}

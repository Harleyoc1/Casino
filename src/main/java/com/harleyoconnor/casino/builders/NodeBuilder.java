package com.harleyoconnor.casino.builders;

import javafx.scene.Node;

/**
 * An interface for a simple JavaFX node builder.
 *
 * @author Harley O'Connor
 */
public interface NodeBuilder<T extends Node> {

    /**
     * @return The actual value of the object the builder has been building.
     */
    T build ();

}

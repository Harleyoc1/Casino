package com.harleyoconnor.casino.builders;

import javax.annotation.Nonnull;

/**
 * @author Harley O'Connor
 */
public interface Builder<T> {

    /**
     * @return The {@link Object} that's been built.
     */
    @Nonnull
    T build ();

}

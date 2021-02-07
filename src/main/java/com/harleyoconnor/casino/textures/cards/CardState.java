package com.harleyoconnor.casino.textures.cards;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Harley O'Connor
 */
public final class CardState {

    private final Card card;
    private boolean hidden;

    public CardState(Card card, boolean hidden) {
        this.card = card;
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return hidden;
    }

    public CardState setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public Card getCard() {
        return card;
    }

    /**
     * Converts a {@link List} of {@link CardState} objects to a {@link List} of {@link Card} objects.
     *
     * @param cardStates The {@link List} of {@link CardState} objects.
     * @return The {@link List} of {@link Card} objects.
     */
    public static List<Card> getCardList(List<CardState> cardStates) {
        return cardStates.stream().map(CardState::getCard).collect(Collectors.toList());
    }

}

package com.harleyoconnor.casino.textures.cards;

import com.harleyoconnor.javautilities.IntegerUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Harley O'Connor
 */
public final class CardDeck {

    private final List<CardState> deck = new ArrayList<>();

    public CardDeck() {
        this.deck.addAll(Cards.CARDS.stream().map(Card::getDefaultState).collect(Collectors.toList()));
    }

    /**
     * Generates and returns a random selection of the given number of cards.
     *
     * @param number The number of cards to select.
     * @return A random selection of the given number of cards.
     */
    public List<CardState> select(int number) {
        final List<CardState> selectedCards = new ArrayList<>();

        // Generate the number of cards required, adding each to the list.
        for (int i = 0; i < number; i++) {
            CardState selected = this.select();
            if (selected != null)
                selectedCards.add(selected);
        }

        return selectedCards;
    }

    /**
     * @return A random card from the deck, or null if none are left.
     */
    @Nullable
    public CardState select() {
        if (this.deck.size() == 0)
            return null;

        CardState selectedCard = this.deck.get(IntegerUtils.getRandomIntBetween(0, this.deck.size() - 1)); // Select a random card.
        this.deck.remove(selectedCard); // Remove the card from the deck so it can't be selected again.

        return selectedCard;
    }

}

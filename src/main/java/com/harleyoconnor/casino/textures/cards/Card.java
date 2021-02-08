package com.harleyoconnor.casino.textures.cards;

import javafx.scene.image.Image;

/**
 * @author Harley O'Connor
 */
public final class Card {

    /** Stores the {@link Card} object's {@link Suit}, such as Hearts or Spades. */
    private final Suit suit;

    /** Stores the {@link Card} object's {@link Rank}, such as Queen, King, or a number. */
    private final Rank rank;

    public Card (Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * @return This {@link Card} object's default state.
     */
    public CardState getDefaultState () {
        return new CardState(this);
    }

    /**
     * @return The {@link Image} object from {@link Cards}.<tt>CARD_TEXTURES</tt> for this card.
     */
    public Image getTexture () {
        return Cards.CARD_TEXTURES.get(this);
    }

    /**
     * Gets the file name of the {@link Card} relative to <tt>textures/cards</tt>.
     *
     * @return The file name of this {@link Card}.
     */
    public String getFileName () {
        return this.rank.getIdentifier() + this.suit.getLetter() + ".png";
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public enum Rank {
        ACE(1, "A"),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(10, "J"),
        QUEEN(10, "Q"),
        KING(10, "K");

        /** The rank's actual integer value (from 1-13). */
        private final int value;

        /** The rank's identifier. For numbers 2-10 it will be the number, where for special cards it's their first letter. */
        private final String identifier;

        Rank(Integer value) {
            this.value = value;
            this.identifier = value.toString();
        }

        Rank(int value, String identifier) {
            this.value = value;
            this.identifier = identifier;
        }

        public int getValue() {
            return value;
        }

        public String getIdentifier() {
            return identifier;
        }
    }

    public enum Suit {
        SPADES('S'),
        HEARTS('H'),
        DIAMONDS('D'),
        CLUBS('C');

        /**
         * Letter for referencing their textures.
         */
        private final char letter;

        Suit(char letter) {
            this.letter = letter;
        }

        public char getLetter() {
            return letter;
        }
    }

}

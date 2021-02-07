package com.harleyoconnor.casino.textures.cards;

import com.harleyoconnor.casino.AppConstants;
import com.harleyoconnor.javautilities.IntegerUtils;
import javafx.scene.image.Image;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Harley O'Connor
 */
public final class Cards {

    private static final String CARDS_PATH = AppConstants.TEXTURES_PATH + "cards/";

    public static final List<Card> CARDS = new ArrayList<>();
    public static final Map<Card, Image> CARD_TEXTURES = new HashMap<>();

    /**
     * Registers standard set of 52 playing cards and loads their textures.
     */
    public static void loadAndRegisterCards() {
        // Register each card to the cards list.
        if (CARDS.size() == 0)
            registerCards();

        // For each card, grab it's texture file and put it into the card texture map.
        if (CARD_TEXTURES.size() != CARDS.size())
            CARDS.forEach(Cards::getAndPutCardTexture);
    }

    /**
     * Loops through each suit and rank, creating and adding {@link Card} object to the <tt>CARDS</tt>
     * list for each combination.
     */
    private static void registerCards () {
        // Register all card types by looping through all suits and all ranks.
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                CARDS.add(new Card(suit, rank));
            }
        }
    }

    /**
     * Grabs the card texture and creates an {@link Image} object for the given {@link Card}, putting it
     * into the <tt>CARD_TEXTURES</tt> map.
     *
     * @param card The {@link Card}.
     */
    private static void getAndPutCardTexture (Card card) {
        // Create the card texture file object.
        File cardTextureFile = new File(CARDS_PATH + card.getFileName());

        // Check the card texture file actually exists.
        if (!cardTextureFile.exists()) {
            throw new RuntimeException("Could not find texture file for card at \"" + cardTextureFile.getPath() + "\".");
        }

        // Put the card and a new Image object with the card's path.
        CARD_TEXTURES.put(card, new Image(AppConstants.FILE_PREFIX + cardTextureFile.getPath()));
    }

    /**
     * Gets a random given number of cards from the cards registry.
     *
     * @param number The number of random cards to generate.
     * @return A set of random cards.
     */
    public static Set<Card> getRandomCards (int number) {
        // As a safety, if the number of cards requested is more than (or equal to) the total amount of cards, just return the cards list.
        if (number >= CARDS.size())
            return new HashSet<>(CARDS);

        Set<Card> randomCards = new HashSet<>();

        for (int i = 0; i < number; i++) {
            // Loop and grab a random card until we find one not already in the set of cards.

            Card randomCard;
            do {
                randomCard = getRandom();
            } while (randomCards.contains(randomCard));

            // Add the random card to the set.
            randomCards.add(randomCard);
        }

        return randomCards;
    }

    /**
     * @return A random {@link Card} from the <tt>CARDS</tt> registry list.
     */
    public static Card getRandom () {
        return CARDS.get(IntegerUtils.getRandomIntBetween(0, CARDS.size() - 1));
    }

    /**
     * Counts the value of each {@link Card} object given into a total.
     *
     * @param cards The list of {@link Card} objects to count the value of.
     * @return The total value of all the {@link Card} objects given.
     */
    public static int countCardsValues (List<Card> cards) {
        AtomicInteger totalValue = new AtomicInteger();
        cards.forEach(card -> totalValue.addAndGet(card.getRank().getValue()));
        return totalValue.get();
    }

}

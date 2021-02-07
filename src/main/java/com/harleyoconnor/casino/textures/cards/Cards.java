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
    private static final String BACK_TEXTURE_PATH = CARDS_PATH + "back.png";

    public static final List<Card> CARDS = new ArrayList<>();
    public static final Map<Card, Image> CARD_TEXTURES = new HashMap<>();

    public static final Image CARD_BACK_TEXTURE = new Image(AppConstants.FILE_PREFIX + getCardTextureFile(BACK_TEXTURE_PATH).getPath());

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
        File cardTextureFile = getCardTextureFile(CARDS_PATH + card.getFileName());

        // Put the card and a new Image object with the card's path.
        CARD_TEXTURES.put(card, new Image(AppConstants.FILE_PREFIX + cardTextureFile.getPath()));
    }

    /**
     * Gets card texture file from path, or crashes if the file does not exist (that should never happen).
     *
     * @param path The path to the card texture file.
     * @return The {@link File} object for the texture.
     */
    private static File getCardTextureFile(String path) {
        // Create the card texture file object.
        File cardTextureFile = new File(path);

        // Check the card texture file actually exists, if not throw runtime exception as we require the card textures.
        if (!cardTextureFile.exists()) {
            throw new RuntimeException("Could not find texture file for card at \"" + cardTextureFile.getPath() + "\".");
        }

        return cardTextureFile;
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

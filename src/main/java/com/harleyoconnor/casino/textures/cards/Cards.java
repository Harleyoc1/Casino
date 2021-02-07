package com.harleyoconnor.casino.textures.cards;

import com.harleyoconnor.casino.AppConstants;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}

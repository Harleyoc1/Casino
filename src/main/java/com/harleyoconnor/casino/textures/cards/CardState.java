package com.harleyoconnor.casino.textures.cards;

import com.harleyoconnor.casino.animations.FlipAnimation;
import com.harleyoconnor.casino.builders.ImageViewBuilder;
import com.harleyoconnor.casino.builders.StackPaneBuilder;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds card information and views.
 *
 * @author Harley O'Connor
 */
public final class CardState {

    /** Width of the cards. */
    public static final int CARD_WIDTH = 110;
    /** Duration of the flip animation (in milliseconds). */
    public static final int FLIP_DURATION = 4000;

    private final Card card;
    private boolean flipped = true;

    // Views
    private FlipAnimation<ImageView> flipAnimation = null;
    private StackPane viewsHolder = null;

    public CardState(Card card) {
        this.card = card;

    }

    public StackPane createAndConfigureView() {
        StackPaneBuilder<StackPane> viewBuilder = StackPaneBuilder.create().minWidth(CARD_WIDTH);
        final ImageView frontView = this.createFrontView();
        final ImageView backView = this.createBackView();

        viewBuilder.add(frontView, backView);

        this.viewsHolder = viewBuilder.build();
        this.flipAnimation = new FlipAnimation<>(frontView, backView, this.viewsHolder, FLIP_DURATION);

        return this.viewsHolder;
    }

    /**
     * Toggles <tt>this.flipped</tt> and tells the {@link FlipAnimation} object (if it has been created)
     * to play the card flip animation.
     */
    public void flip() {
        this.flipped = !this.flipped;

        if (this.flipAnimation != null)
            this.flipAnimation.play();
    }

    public ImageView createFrontView () {
        return ImageViewBuilder.create().image(this.card.getTexture()).width(CARD_WIDTH).preserveRatio().build();
    }

    public ImageView createBackView () {
        return ImageViewBuilder.create().image(Cards.CARD_BACK_TEXTURE).width(CARD_WIDTH).preserveRatio().build();
    }

    public Card getCard() {
        return card;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public FlipAnimation<ImageView> getFlipAnimation() {
        return flipAnimation;
    }

    @Nullable
    public StackPane getViewsHolder() {
        return viewsHolder;
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

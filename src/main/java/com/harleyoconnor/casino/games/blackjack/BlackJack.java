package com.harleyoconnor.casino.games.blackjack;

import com.harleyoconnor.casino.AppConstants;
import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.builders.*;
import com.harleyoconnor.casino.games.Game;
import com.harleyoconnor.casino.games.Games;
import com.harleyoconnor.casino.games.Player;
import com.harleyoconnor.casino.menus.GamesMenuScreen;
import com.harleyoconnor.casino.menus.MenuScreen;
import com.harleyoconnor.casino.textures.cards.Card;
import com.harleyoconnor.casino.textures.cards.CardDeck;
import com.harleyoconnor.casino.textures.cards.CardState;
import com.harleyoconnor.casino.utils.InterfaceUtils;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harley O'Connor
 */
public final class BlackJack extends Game {

    private static final int BUTTON_WIDTH = 80;

    /** The {@link CardDeck}, which creating a set of cards to randomly select from. */
    private final CardDeck deck = new CardDeck();

    /** The dealer's cards. */
    private final List<CardState> dealerCards = new ArrayList<>();
    /** The player's cards. */
    private final List<CardState> playerCards = new ArrayList<>();

    // Card display builders.
    private final HBoxBuilder<HBox> dealerCardsDisplay = HBoxBuilder.create();
    private final HBoxBuilder<HBox> playerCardsDisplay = HBoxBuilder.create();

    // Label builders.
    private final LabelBuilder<Label> valueLabel = LabelBuilder.create();
    private final LabelBuilder<Label> betLabel = LabelBuilder.create();

    /** If true, one of the dealer's cards has already been left un-flipped. */
    private boolean oneDealerCardLeftHidden = false;

    /** Holds timelines for animations. */
    private final List<Timeline> timelines = new ArrayList<>();
    /** Holds the index of the next animation to be played. */
    private int nextAnimationIndex = 0;

    public BlackJack(Casino casino, Stage stage, Scene scene, MenuScreen previousScreen, Player player) {
        super(casino, stage, scene, previousScreen, player, Games.BLACKJACK);
    }

    @Override
    protected Pane setupScreen() {
        // Selects 2 random cards for the dealer.
        this.dealerCards.addAll(this.deck.select(2));

        // Create the card views and adds to the dealers card display.
        this.dealerCards.forEach(cardState -> this.dealerCardsDisplay.add(this.createCardView(cardState, true)));

        this.playerCards.addAll(this.deck.select(2));

        this.playerCards.forEach(cardState -> this.playerCardsDisplay.add(this.createCardView(cardState)));

        // Components for user's view.
        this.valueLabel.text(this.getValueLabelText()).styleClasses(AppConstants.WHITE_TEXT).body();
        this.betLabel.text(this.getBetLabelText()).styleClasses(AppConstants.WHITE_TEXT).body();
        Button hitButton = ButtonBuilder.create().text("Hit").onAction(this::onHitPress).fixWidth(BUTTON_WIDTH).body().build();
        Button standButton = ButtonBuilder.create().text("Stand").onAction(this::onStandPress).fixWidth(BUTTON_WIDTH).body().build();
        Button surrenderButton = ButtonBuilder.create().text("Surrender").onAction(this::onQuitPress).fixWidth(BUTTON_WIDTH).build();

        return VBoxBuilder.create().add(this.dealerCardsDisplay.spacing(10).padding().centre().build(), InterfaceUtils.createVerticalSpacer(),
                InterfaceUtils.createVerticalSpacer(), this.playerCardsDisplay.spacing(10).padding().centre().build(),
                InterfaceUtils.centreHorizontally(0, 0, this.valueLabel.build()), InterfaceUtils.centreHorizontally(0, 0, this.betLabel.build()),
                InterfaceUtils.centreHorizontally(hitButton, standButton, surrenderButton))
                .centre().build();
    }

    private void onHitPress (ActionEvent event) {
        if (this.isAnimationPlaying())
            return;

        final CardState cardState = this.deck.select();

        this.playerCardsDisplay.build().getChildren().forEach(node -> {
            if (node instanceof StackPane) {
                StackPane currentCardView = (StackPane) node;
                StackPaneBuilder.edit((currentCardView)).translateX(CardState.CARD_WIDTH - 50);
                this.timelines.add(TimelineBuilder.create().keyFrame(750, currentCardView.translateXProperty(), 0, Interpolator.EASE_BOTH).onFinished(actionEvent -> {
                    this.playNextAnimation();
                }).build());
            }
        });

        this.playerCards.add(cardState);
        this.playerCardsDisplay.insert(this.createCardView(cardState), this.playerCardsDisplay.build().getChildren().size() - 1);

        this.playNextAnimation();
        this.valueLabel.text(this.getValueLabelText());
    }

    private void onStandPress (ActionEvent event) {
        if (this.isAnimationPlaying())
            return;
    }

    private void onQuitPress (ActionEvent event) {
        if (this.isAnimationPlaying())
            return;

        new GamesMenuScreen(this.casino, this.stage, this.scene, this).show();
    }

    private StackPane createCardView(CardState cardState) {
        return this.createCardView(cardState, false);
    }

    private StackPane createCardView(CardState cardState, boolean dealerCard) {
        StackPane cardView = StackPaneBuilder.edit(cardState.createAndConfigureView()).translateY(-this.scene.getHeight()).build();

        // Set up animations for the card.
        this.timelines.add(TimelineBuilder.create().keyFrame(1500, cardView.translateYProperty(), 0, Interpolator.EASE_BOTH)
                .onFinished(event -> {
                    if (dealerCard && !this.oneDealerCardLeftHidden)
                        this.oneDealerCardLeftHidden = true;
                    else cardState.flip();
                    this.playNextAnimation();
                }).build());

        return cardView;
    }

    /**
     * @return The text to display in the value label.
     */
    private String getValueLabelText() {
        return "Your Value: " + this.countCardsValues(CardState.getCardList(this.playerCards));
    }

    /**
     * @return The text to display in the bet label.
     */
    private String getBetLabelText() {
        return "Bet: " + this.player.getAmountBet() + " BTC";
    }

    /**
     * Counts the value of the {@link Card} objects given, including calculating "hard" and "soft" aces.
     *
     * @param cards The {@link List} of {@link Card} objects.
     * @return The total value of the cards.
     */
    private int countCardsValues (List<Card> cards) {
        int value = 0, aceCount = 0;

        for (Card card : cards) {
            if (card.getRank() == Card.Rank.ACE) {
                aceCount++; // If the card was an ace, add one to the ace count so we can handle it after.
            } else value += card.getRank().getValue(); // If the card wasn't an ace, add its value from the Rank.
        }

        // Handle special case for aces - must be done after counting all other cards
        for (int i = 0; i < aceCount; i++) {
            if (value > 10) {
                value++; // Ace is hard, with a value of 1.
            } else value += 11; // Ace is soft, with a value of 11.
        }

        return value;
    }

    @Override
    public void show() {
        super.show();

        // Start playing card animations.
        this.playNextAnimation();
    }

    private boolean isAnimationPlaying () {
        return this.timelines.size() > 0;
    }

    private void playNextAnimation() {
        if (this.nextAnimationIndex >= this.timelines.size()) {
            this.clearTimelines();
            return;
        }

        this.timelines.get(this.nextAnimationIndex).play();
        this.nextAnimationIndex++;
    }

    private void clearTimelines () {
        this.nextAnimationIndex = 0;
        this.timelines.clear();
    }

}

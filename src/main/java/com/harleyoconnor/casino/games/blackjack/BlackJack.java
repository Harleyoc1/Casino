package com.harleyoconnor.casino.games.blackjack;

import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.animations.Animation;
import com.harleyoconnor.casino.animations.SlideAnimation;
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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    private static final int BUTTON_WIDTH = 120;

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

    /** Holds animations. */
    private final List<Animation> animations = new ArrayList<>();
    /** Holds the index of the next animation to be played. */
    private int nextAnimationIndex = 0;

    /** Holds the current turn. */
    private int turnCount = 0;

    private final BooleanProperty surrenderDisabled = new SimpleBooleanProperty(false);

    private boolean playerDrawnOnce = false;

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
        this.valueLabel.text(this.getValueLabelText()).body();
        this.betLabel.text(this.getBetLabelText()).body();

        Button hitButton = ButtonBuilder.create().text("Hit").onAction(this::onHitPress).fixWidth(BUTTON_WIDTH).body().build();
        Button standButton = ButtonBuilder.create().text("Stand").onAction(this::onStandPress).fixWidth(BUTTON_WIDTH).body().build();
        Button doubleDownButton = ButtonBuilder.create().text("Double Down").onAction(this::onDoubleDownPress).fixWidth(BUTTON_WIDTH).body().build();
        Button surrenderButton = ButtonBuilder.create().text("Surrender").onAction(this::onSurrenderPress).fixWidth(BUTTON_WIDTH).build();

        surrenderButton.disableProperty().bind(this.surrenderDisabled);

        return VBoxBuilder.create().add(this.dealerCardsDisplay.spacing(10).padding().centre().build(), InterfaceUtils.createVerticalSpacer(),
                InterfaceUtils.createVerticalSpacer(), this.playerCardsDisplay.spacing(10).padding().centre().build(),
                InterfaceUtils.centreHorizontally(0, 0, this.valueLabel.build()), InterfaceUtils.centreHorizontally(0, 0, this.betLabel.build()),
                InterfaceUtils.centreHorizontally(hitButton, standButton, doubleDownButton, surrenderButton))
                .centre().build();
    }

    /**
     * Executes when the hit button is pressed, making the player's cards slide right and drawing another
     * onto their section.
     *
     * @param event The {@link ActionEvent}.
     */
    private void onHitPress (ActionEvent event) {
        if (this.isAnimationPlaying())
            return;

        this.drawNewCard(this.playerCards, this.playerCardsDisplay); // Draw a new card.
        this.valueLabel.text(this.getValueLabelText()); // Update the value label.

        this.playerDrawnOnce = true;
        this.nextTurn();
    }

    private void onStandPress (ActionEvent event) {
        if (this.isAnimationPlaying())
            return;

        this.nextTurn();
    }

    /**
     * Executes when the double down button is pressed, doubling the player's bet and updating the bet
     * label with the new amount.
     *
     * @param event The {@link ActionEvent}.
     */
    private void onDoubleDownPress (ActionEvent event) {
        if (this.isAnimationPlaying() || this.playerDrawnOnce)
            return;

        this.player.setAmountBet(this.player.getAmountBet() * 2); // Double the player's bet.
        this.betLabel.text(this.getBetLabelText()); // Update the bet label to show the new bet value.
    }

    /**
     * Executes when the surrender button is pressed, taking the player back to the game menu screen.
     *
     * @param event The {@link ActionEvent}.
     */
    private void onSurrenderPress(ActionEvent event) {
        if (this.isAnimationPlaying() || this.turnCount > 0)
            return;

        this.player.setAmountBet(this.player.getAmountBet() / 2); // Half the player's bet.
        this.endGame(EndType.PLAYER_SURRENDER); // End the game.

        new GamesMenuScreen(this.casino, this.stage, this.scene, this).show();
    }

    private void drawNewCard (List<CardState> cardStates, HBoxBuilder<HBox> cardsView) {
        // Make animation for cards sliding to the left (to make room for new card).
        cardStates.forEach(cardState -> {
            // Get the view holder for the current card.
            StackPane cardView = cardState.getViewsHolder();

            // Translate the card so that it doesn't appear to move when the new card is added.
            StackPaneBuilder.edit(cardView).translateX(CardState.CARD_WIDTH - 50);

            // Create and add the animation to the animation list.
            this.animations.add(new SlideAnimation<>(cardView, SlideAnimation.TranslateAxis.X, 0, 750, Interpolator.EASE_BOTH)
                    .setOnFinish(this::onAnimationFinished));
        });

        final CardState cardState = this.deck.select(); // Select a new card from the deck.
        cardStates.add(cardState); // Add it to the card list.

        // Insert it into the card display.
        cardsView.insert(this.createCardView(cardState), cardsView.build().getChildren().size() - 1);

        if (this.animations.size() <= cardStates.size())
            this.playNextAnimation(); // Play the animation.
    }

    private void nextTurn() {
        int dealerCardValue = this.countCardsValues(this.dealerCards);

        if (dealerCardValue < 17) {
            // If dealer's card value is less than 17, they hit.
            this.drawNewCard(this.dealerCards, this.dealerCardsDisplay);
        }
        // If dealer's card value is 17 or more, they stand (do nothing).

        // End the game if the dealer has won or bust.
        this.endGameIfWonOrLost(dealerCardValue, true);
        this.endGameIfWonOrLost(this.countCardsValues(this.playerCards), false);

        this.turnCount++; // Increment turn count by one.

        if (this.turnCount > 0) {
            // After the first turn, disable surrender option.
            this.surrenderDisabled.setValue(true);
        }
    }

    /**
     * If the card value given makes the player or dealer (depending on boolean given) lose or win,
     * end game with the relevant end type.
     *
     * @param cardValue The value of the player/dealer cards.
     * @param dealer True if the card value given is the dealer's.
     */
    private void endGameIfWonOrLost(int cardValue, boolean dealer) {
        if (cardValue == 21) {
            // The player/dealer has won - their card value equals 21.
            this.endGame(dealer ? EndType.DEALER_WON : EndType.PLAYER_WON);
        } else if (cardValue > 21) {
            // The player/dealer has bust - they card value has gone over 21.
            this.endGame(dealer ? EndType.DEALER_BUST : EndType.PLAYER_BUST);
        }
    }

    private StackPane createCardView(CardState cardState) {
        return this.createCardView(cardState, false);
    }

    /**
     * Creates and configures a view for a card, also creating animations.
     *
     * @param cardState The {@link CardState} object for the card.
     * @param dealerCard If the card being added is the dealer's, this should be true.
     * @return The configured {@link StackPane} view.
     */
    private StackPane createCardView(CardState cardState, boolean dealerCard) {
        StackPane cardView = StackPaneBuilder.edit(cardState.createAndConfigureView()).translateY(-this.scene.getHeight()).build();
        // Set up card animation, so that it slides in from the top.
        this.animations.add(new SlideAnimation<>(cardView, SlideAnimation.TranslateAxis.Y, 0, 1500, Interpolator.EASE_BOTH)
                .setOnFinish(event -> this.onCardAnimationFinished(event, cardState, dealerCard)));
        return cardView;
    }

    /**
     * Executes when a card sliding animation has finished, flipping the card (if it should be flipped).
     *
     * @param event The {@link ActionEvent}.
     * @param cardState The {@link CardState} object for the card.
     * @param dealerCard If the card is the dealer's, this should be true.
     */
    private void onCardAnimationFinished (ActionEvent event, CardState cardState, boolean dealerCard) {
        if (dealerCard && !this.oneDealerCardLeftHidden) {
            // If the card is the dealer's and we haven't already left one hidden, don't flip the card.
            this.oneDealerCardLeftHidden = true;
            // Play the next animation.
            this.onAnimationFinished(event);
        } else {
            // Make sure next animation is played after flip has finished.
            cardState.getFlipAnimation().setOnFinish(this::onAnimationFinished);
            // Flip the card and play the flipping animation.
            cardState.flip();
        }
    }

    /**
     * {@link EventHandler} lambda method that plays the next animation.
     *
     * @param event The {@link ActionEvent}.
     */
    private void onAnimationFinished (ActionEvent event) {
        this.playNextAnimation();
    }

    /**
     * @return The text to display in the value label.
     */
    private String getValueLabelText() {
        return "Your Value: " + this.countCardsValues(this.playerCards);
    }

    /**
     * @return The text to display in the bet label.
     */
    private String getBetLabelText() {
        return "Bet: " + this.player.getAmountBet() + " BTC";
    }

    /**
     * Counts the value of the {@link Card} objects from the {@link CardState} objects given,
     * including calculating "hard" and "soft" aces.
     *
     * @param cardStates The {@link List} of {@link CardState} objects.
     * @return The total value of the cards.
     */
    private int countCardsValues (List<CardState> cardStates) {
        List<Card> cards = CardState.getCardList(cardStates);
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

        this.playNextAnimation(); // Start playing animations.
    }

    /**
     * @return True if an animation is currently playing.
     */
    private boolean isAnimationPlaying () {
        return this.animations.size() > 0;
    }

    /**
     * Plays the next animation, or stops playing animations if they have all been played.
     */
    private void playNextAnimation() {
        if (this.nextAnimationIndex >= this.animations.size()) {
            // If all animations have been played, clear the animations and return.
            this.clearAnimations();
            return;
        }

        // Play the next animation and increment the next animation index.
        this.animations.get(this.nextAnimationIndex).play();
        this.nextAnimationIndex++;
    }

    /**
     * Clears animations and resets <tt>nextAnimationIndex</tt>.
     */
    private void clearAnimations() {
        this.nextAnimationIndex = 0;
        this.animations.clear();
    }

    private void endGame (EndType endType) {
        if (endType.doesPlayerWinBet()) {
            this.player.betWon();
        } else {
            this.player.betLost();
        }
    }

    public enum EndType {
        PLAYER_WON, PLAYER_BUST, PLAYER_SURRENDER, DEALER_WON, DEALER_BUST;

        public boolean doesPlayerWinBet () {
            return this == PLAYER_WON || this == DEALER_BUST;
        }
    }

}

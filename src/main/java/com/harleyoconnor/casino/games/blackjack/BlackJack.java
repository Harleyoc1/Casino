package com.harleyoconnor.casino.games.blackjack;

import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.animations.Animation;
import com.harleyoconnor.casino.animations.SlideAnimation;
import com.harleyoconnor.casino.animations.TranslateAxis;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    /** The player's cards which are currently shown. */
    private final List<CardState> visiblePlayerCards = new ArrayList<>();

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

    private final BooleanProperty doubleDownDisabled = new SimpleBooleanProperty(false);
    private final BooleanProperty surrenderDisabled = new SimpleBooleanProperty(false);

    public BlackJack(Casino casino, Stage stage, Scene scene, StackPane parentView, MenuScreen previousScreen, Player player) {
        super(casino, stage, scene, parentView, previousScreen, player, Games.BLACKJACK);
    }

    @Override
    protected Pane setupScreen() {
        // Selects 2 random cards for the dealer.
        this.dealerCards.addAll(this.deck.select(2));

        // Create the card views and adds to the dealers card display.
        this.dealerCards.forEach(cardState -> this.dealerCardsDisplay.add(this.createCardView(cardState, true)));

        this.playerCards.addAll(this.deck.select(2));

        this.playerCards.forEach(cardState -> this.playerCardsDisplay.add(this.createCardView(cardState, false)));

        // Components for user's view.
        this.updateValueLabel().body();
        this.betLabel.text(this.getBetLabelText()).body();

        Button hitButton = ButtonBuilder.create().text("Hit").onAction(this::onHitPress).fixWidth(BUTTON_WIDTH).body().build();
        Button standButton = ButtonBuilder.create().text("Stand").onAction(this::onStandPress).fixWidth(BUTTON_WIDTH).body().build();
        Button doubleDownButton = ButtonBuilder.create().text("Double Down").onAction(this::onDoubleDownPress).fixWidth(BUTTON_WIDTH).body().build();
        Button surrenderButton = ButtonBuilder.create().text("Surrender").onAction(this::onSurrenderPress).fixWidth(BUTTON_WIDTH).build();

        doubleDownButton.disableProperty().bind(this.doubleDownDisabled);
        surrenderButton.disableProperty().bind(this.surrenderDisabled);

        VBox mainContentVBox = VBoxBuilder.create().add(this.dealerCardsDisplay.spacing(10).padding().centre().build(), InterfaceUtils.createVerticalSpacer(),
                InterfaceUtils.createVerticalSpacer(), this.playerCardsDisplay.spacing(10).padding().centre().build(),
                InterfaceUtils.centreHorizontally(0, 0, this.valueLabel.build()), InterfaceUtils.centreHorizontally(0, 0, this.betLabel.build()),
                InterfaceUtils.centreHorizontally(hitButton, standButton, doubleDownButton, surrenderButton))
                .centre().build();

        return StackPaneBuilder.create().add(mainContentVBox).build();
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

        this.drawNewCard(this.playerCards, this.playerCardsDisplay, false); // Draw a new card.

        this.doubleDownDisabled.setValue(true);
        this.nextTurn();
    }

    /**
     * Executes when the player chooses to stand, calling the next turn. Also ends the game if nothing
     * happened as a result of the call to nextTurn.
     *
     * @param event The {@link ActionEvent}.
     */
    private void onStandPress (ActionEvent event) {
        if (this.isAnimationPlaying())
            return;

        this.nextTurn();

        // If an animation is not playing, nothing happened.
        if (!this.isAnimationPlaying()) {
            int dealerCardValue = this.countCardsValues(this.dealerCards);
            int playerCardValue = this.countCardsValues(this.playerCards);

            if (dealerCardValue > playerCardValue) { // If the dealer is higher (closer to 21), they win.
                this.endGame(EndType.DEALER_CLOSER);
            } else if (dealerCardValue == playerCardValue) { // If their values are equal, it is a tie.
                this.endGame(EndType.TIE);
            } else { // Otherwise, the player's value is higher so they win.
                this.endGame(EndType.PLAYER_CLOSER);
            }
        }
    }

    /**
     * Executes when the double down button is pressed, doubling the player's bet and updating the bet
     * label with the new amount.
     *
     * @param event The {@link ActionEvent}.
     */
    private void onDoubleDownPress (ActionEvent event) {
        if (this.isAnimationPlaying())
            return;

        this.doubleDownDisabled.setValue(true); // Only allow player to double down once.
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
    }

    /**
     * Draws a new card from the deck, shifting other cards over the necessary amount.
     *
     * @param cardStates The {@link CardState} objects.
     * @param cardsView The {@link HBoxBuilder} for the {@link HBox} to add the card to.
     * @param dealer This should be true if the card is being drawn for the deaeler.
     */
    private void drawNewCard (List<CardState> cardStates, HBoxBuilder<HBox> cardsView, boolean dealer) {
        // Make animation for cards sliding to the left (to make room for new card).
        cardStates.forEach(cardState -> {
            // Get the view holder for the current card.
            StackPane cardView = cardState.getViewsHolder();

            // Translate the card so that it doesn't appear to move when the new card is added.
            StackPaneBuilder.edit(cardView).translateX(CardState.CARD_WIDTH - 50);

            // Create and add the animation to the animation list.
            this.animations.add(new SlideAnimation<>(cardView, TranslateAxis.X, 0, 750, Interpolator.EASE_BOTH)
                    .setOnFinish(this::onAnimationFinished));
        });

        final CardState cardState = this.deck.select(); // Select a new card from the deck.
        cardStates.add(cardState); // Add it to the card list.

        // Insert it into the card display.
        cardsView.insert(this.createCardView(Objects.requireNonNull(cardState), dealer), cardsView.build().getChildren().size() - 1);

        if (this.animations.size() <= cardStates.size())
            this.playNextAnimation(); // Play the animation.
    }

    /**
     * Executes the next turn.
     */
    private void nextTurn() {
        int dealerCardValue = this.countCardsValues(this.dealerCards);

        if (dealerCardValue < 17) {
            // If dealer's card value is less than 17, they hit.
            this.drawNewCard(this.dealerCards, this.dealerCardsDisplay, true);
        }
        // If dealer's card value is 17 or more, they stand (do nothing).

        dealerCardValue = this.countCardsValues(this.dealerCards);

        // End the game if the dealer has won or bust, and then if the game wasn't ended end the game if the player won or bust.
        if (!this.endGameIfWonOrLost(dealerCardValue, true))
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
     * @return True if game was ended.
     */
    private boolean endGameIfWonOrLost(int cardValue, boolean dealer) {
        if (cardValue == 21) {
            // The player/dealer has won - their card value equals 21.
            this.endGame(dealer ? EndType.DEALER_WON : EndType.PLAYER_WON);
        } else if (cardValue > 21) {
            // The player/dealer has bust - they card value has gone over 21.
            this.endGame(dealer ? EndType.DEALER_BUST : EndType.PLAYER_BUST);
        }

        return cardValue >= 21;
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
        this.animations.add(new SlideAnimation<>(cardView, TranslateAxis.Y, 0, 1500, Interpolator.EASE_BOTH)
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
            // Flip the card and play the flipping animation.
            if (!this.showCard(cardState, !dealerCard, false))
                this.onCardFlipFinished(event, cardState, !dealerCard);
        }
    }

    /**
     * Executes when a card flip animation has finished, adding it to the visible player card list
     * and updating the value label if it's a player card, and plays the next animation.
     *
     * @param event The {@link ActionEvent}.
     * @param cardState The {@link CardState} for the card.
     * @param playerCard True if the card is a player's.
     */
    private void onCardFlipFinished (ActionEvent event, CardState cardState, boolean playerCard) {
        if (playerCard) {
            this.visiblePlayerCards.add(cardState);
            this.updateValueLabel();
        }

        this.onAnimationFinished(event);
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
     * Updates text in value label to display the player's current hand value.
     */
    private LabelBuilder<Label> updateValueLabel() {
        return this.valueLabel.text("Your Value: " + this.countCardsValues(this.visiblePlayerCards));
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
    public void onSlideInFinished(ActionEvent event) {
        super.onSlideInFinished(event);

        this.endGameIfWonOrLost(this.countCardsValues(this.dealerCards), true); // The dealer draws their cards first.
        this.endGameIfWonOrLost(this.countCardsValues(this.playerCards), false); // Check if the player won.
        this.playNextAnimation(); // Start playing animations when the scene is shown.
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
        if (this.animations.size() > this.nextAnimationIndex)
            this.animations.get(this.nextAnimationIndex).stop();

        this.nextAnimationIndex = 0;
        this.animations.clear();
    }

    /**
     * Ends the game with the given {@link EndType}.
     *
     * @param endType The {@link EndType} with which the game ended.
     */
    private void endGame (EndType endType) {
        this.playerCards.forEach(this::showCard); // Show all of the player's cards (they should be shown anyway but just in case).
        this.dealerCards.forEach(this::showCard); // Show all of the dealer's cards.

        if (endType.doesPlayerWinBet()) {
            // If the player won or the dealer is bust, they win the bet.
            this.player.betWon();
        } else if (endType != EndType.TIE) {
            // If the end type is a tie, they keep their bet. Otherwise they lose it.
            this.player.betLost();
        }

        // Create result box.
        final VBoxBuilder<VBox> resultBoxBuilder = VBoxBuilder.create().add(InterfaceUtils.centreHorizontally(LabelBuilder.create().text(endType.getTitle()).styleClasses("black-text").title().wrapText().build()))
                .add(InterfaceUtils.centreHorizontally(LabelBuilder.create().text(endType.getSubtitle()).styleClasses("black-text").body().wrapText().build()));

        // This will store the second line of text, which will inform the user of the outcome of their bet.
        final HBox middleTextDisplay;

        if (endType != EndType.TIE) {
            // If it wasn't a tie, show them their earnings or losses.
            middleTextDisplay = InterfaceUtils.centreHorizontally(LabelBuilder.create().text((endType.doesPlayerWinBet() ? "Earnings" : "Losses") + ": " +
                    this.player.getAmountBet() + " BTC").styleClasses("black-text").body().wrapText().build());
        } else {
            // If it was a tie, tell them that they get to keep their bet.
            middleTextDisplay = InterfaceUtils.centreHorizontally(LabelBuilder.create().text("You keep your bet of " + this.player.getAmountBet() + " BTC.").styleClasses("black-text").body().wrapText().build());
        }

        // Finish building the result box. Translate it a screen up so it's out of view initially.
        resultBoxBuilder.add(middleTextDisplay).add(HBoxBuilder.create().add(ButtonBuilder.create().text("Rematch").onAction(this::onRematchPress).body().styleClasses("black-border").build(), ButtonBuilder.create().text("Quit").onAction(this::onQuitPress).body().styleClasses("black-border").build())
                .centre().spacing().build()).translateY(-this.scene.getHeight()).styleClasses("game-end-stats").centre().padding().maxWidthHeight(260);

        this.layout.getChildren().add(resultBoxBuilder.build()); // Add result box to the view.

        // Create the animation so the result box slides in from the top.
        this.animations.add(new SlideAnimation<>(resultBoxBuilder.build(), TranslateAxis.Y, 0, 1000, Interpolator.EASE_BOTH).setOnFinish(this::onAnimationFinished));

        if (!this.isAnimationPlaying())
            this.playNextAnimation(); // Play next animation if not already playing.
    }

    private void onRematchPress (ActionEvent event) {
        // Create a new BlackJack game with the same original bet.
        this.toNewScreen(new BlackJack(this.casino, this.stage, this.scene, this.parentView, this, this.player.resetBet()), TranslateAxis.X, true);
    }

    private void onQuitPress (ActionEvent event) {
        // Show the game menu screen.
        this.toNewScreen(new GamesMenuScreen(this.casino, this.stage, this.scene, this.parentView, this));
    }

    /**
     * If the card isn't already flipped, this flips it, and assumes the given card is a player card.
     *
     * @param cardState The {@link CardState} object.
     */
    private void showCard (CardState cardState) {
        this.showCard(cardState, false, true);
    }

    /**
     * If the card isn't already flipped, this flips it.
     *
     * @param cardState The {@link CardState} object.
     * @param playerCard True if the card being shown is a player card.
     * @return True if the card was flipped.
     */
    private boolean showCard(CardState cardState, boolean playerCard, boolean gameEnding) {
        // If the card is not shown (flipped) and it's not currently translated (still sliding in), then flip it.
        if (cardState.isFlipped() && cardState.getViewsHolder() != null && cardState.getViewsHolder().getTranslateY() == 0) {
            // If an animation isn't currently playing, make sure to call the next animation after this one.
            if (this.isAnimationPlaying())
                cardState.getFlipAnimation().setOnFinish(event -> this.onCardFlipFinished(event, cardState, playerCard));
            else if (gameEnding && !this.isAnimationPlaying()) cardState.getFlipAnimation().setOnFinish(this::onAnimationFinished);

            cardState.flip(); // Flip the card.
            return true;
        }
        return false;
    }

    /**
     * A simple enum that stores the way with which the game ended, and the message that should be displayed as a result.
     */
    public enum EndType {
        PLAYER_WON("You won!", "You got a card value of 21."),
        PLAYER_BUST("Busted!", "Your card value went over 21."),
        PLAYER_CLOSER("You won!", "You both stood, and your card value was closer to 21."),
        PLAYER_SURRENDER("You surrendered!", "You craven."),
        DEALER_WON("The dealer won!", "The dealer got a card value of 21."),
        DEALER_BUST("The dealer went bust!", "The dealer's card value went over 21."),
        DEALER_CLOSER("The dealer won!", "You both stood, and the dealer's card value was closer to 21."),
        TIE("Tie!", "You both ended up with the same card value.");

        /** The title of the message sent to the player when the game ends. */
        private final String title;
        /** The message about how the end came about, to be displayed when the game ends. */
        private final String subtitle;

        EndType(String title, String subtitle) {
            this.title = title;
            this.subtitle = subtitle;
        }

        /**
         * @return True if this {@link EndType} results in the player winning the bet.
         */
        public boolean doesPlayerWinBet () {
            return this == PLAYER_WON || this == DEALER_BUST || this == PLAYER_CLOSER;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }
    }

}

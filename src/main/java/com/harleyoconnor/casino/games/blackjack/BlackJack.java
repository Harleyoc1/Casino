package com.harleyoconnor.casino.games.blackjack;

import com.harleyoconnor.casino.AppConstants;
import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.builders.*;
import com.harleyoconnor.casino.games.Game;
import com.harleyoconnor.casino.games.Games;
import com.harleyoconnor.casino.games.Player;
import com.harleyoconnor.casino.menus.MenuScreen;
import com.harleyoconnor.casino.textures.cards.CardDeck;
import com.harleyoconnor.casino.textures.cards.CardState;
import com.harleyoconnor.casino.textures.cards.Cards;
import com.harleyoconnor.casino.utils.InterfaceUtils;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harley O'Connor
 */
public final class BlackJack extends Game {

    private List<Timeline> timelines;

    private CardDeck deck;

    private List<CardState> dealerCards;
    private List<CardState> playerCards;

    private boolean oneDealerCardLeftHidden;

    public BlackJack(Casino casino, Stage stage, Scene scene, MenuScreen previousScreen, Player player) {
        super(casino, stage, scene, previousScreen, player, Games.BLACKJACK);
    }

    @Override
    protected Pane setupScreen() {
        this.deck = new CardDeck();

        this.timelines = new ArrayList<>();
        this.dealerCards = new ArrayList<>();
        this.playerCards = new ArrayList<>();

        this.oneDealerCardLeftHidden = false;

        this.dealerCards.addAll(this.deck.select(2)); // Select random cards for the dealer.

        HBoxBuilder<HBox> dealerCardsDisplayBuilder = HBoxBuilder.create();

        this.dealerCards.forEach(cardState -> dealerCardsDisplayBuilder.add(this.createCardView(cardState, true)));

        HBox dealersCardsDisplay = dealerCardsDisplayBuilder.spacing(10).padding().centre().build();

        this.playerCards.addAll(this.deck.select(2));
        HBoxBuilder<HBox> playerCardsDisplayBuilder = HBoxBuilder.create();

        this.playerCards.forEach(cardState -> playerCardsDisplayBuilder.add(this.createCardView(cardState)));

        // Starts playing card animations.
        this.playAnimation(0);

        HBox usersCardsDisplay = playerCardsDisplayBuilder.spacing(10).padding().centre().build();

        // Components for user's view.
        Label valueLabel = LabelBuilder.create().text(this.getValueLabelText()).styleClasses(AppConstants.WHITE_TEXT).body().build();
        Button hitButton = ButtonBuilder.create().text("Hit").fixWidth(65).body().build();
        Button standButton = ButtonBuilder.create().text("Stand").fixWidth(65).body().build();

        return VBoxBuilder.create().add(dealersCardsDisplay, InterfaceUtils.createVerticalSpacer(), InterfaceUtils.createVerticalSpacer(), usersCardsDisplay,
                HBoxBuilder.create().add(valueLabel).centre().build(), HBoxBuilder.create().add(hitButton, standButton).spacing().padding().centre().build())
                .centre().build();
    }

    private StackPane createCardView(CardState cardState) {
        return this.createCardView(cardState, false);
    }

    private StackPane createCardView(CardState cardState, boolean dealerCard) {
        StackPane cardView = StackPaneBuilder.edit(cardState.createAndConfigureView()).translateY(-this.scene.getHeight()).build();

        // Set up animations for the card.
        int nextIndex = this.timelines.size() + 1;
        this.timelines.add(TimelineBuilder.create().keyFrame(1500, cardView.translateYProperty(), 0, Interpolator.EASE_BOTH)
                .onFinished(event -> {
                    if (dealerCard && !this.oneDealerCardLeftHidden)
                        this.oneDealerCardLeftHidden = true;
                    else cardState.flip();
                    this.playAnimation(nextIndex);
                }).build());

        return cardView;
    }

    private String getValueLabelText () {
        return "Your Value: " + Cards.countCardsValues(CardState.getCardList(this.playerCards));
    }

    private void playAnimation(int index) {
        if (index >= this.timelines.size())
            return;
        this.timelines.get(index).play();
    }

}

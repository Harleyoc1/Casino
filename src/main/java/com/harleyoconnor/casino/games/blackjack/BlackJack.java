package com.harleyoconnor.casino.games.blackjack;

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
import com.harleyoconnor.javautilities.IntegerUtils;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

    public BlackJack(Casino casino, Stage stage, Scene scene, MenuScreen previousScreen, Player player) {
        super(casino, stage, scene, previousScreen, player, Games.BLACKJACK);
    }

    @Override
    protected Pane setupScreen() {
        this.deck = new CardDeck();

        this.timelines = new ArrayList<>();
        this.dealerCards = new ArrayList<>();
        this.playerCards = new ArrayList<>();

        this.dealerCards.addAll(this.deck.select(2)); // Select random cards for the dealer.
        this.dealerCards.get(IntegerUtils.getRandomIntBetween(0, this.dealerCards.size() - 1)).setHidden(true); // Hide one of the dealer's cards.

        HBoxBuilder<HBox> dealerCardsDisplayBuilder = HBoxBuilder.create();

        this.dealerCards.forEach(cardState -> dealerCardsDisplayBuilder.add(this.createCardView(cardState)));

        HBox dealersCardsDisplay = dealerCardsDisplayBuilder.spacing(10).padding().centre().build();

        this.playerCards.addAll(this.deck.select(2));
        HBoxBuilder<HBox> playerCardsDisplayBuilder = HBoxBuilder.create();

        this.playerCards.forEach(cardState -> playerCardsDisplayBuilder.add(this.createCardView(cardState)));

        // Starts playing card animations.
        this.playAnimation(0);

        HBox usersCardsDisplay = playerCardsDisplayBuilder.spacing(10).padding().centre().build();

        // Components for user's view.
        Label valueLabel = LabelBuilder.create().text(this.getValueLabelText()).styleClasses("white").body().build();
        Button hitButton = ButtonBuilder.create().text("Hit").styleClasses("white-button").fixWidth(65).body().build();
        Button standButton = ButtonBuilder.create().text("Stand").styleClasses("white-button").fixWidth(65).body().build();

        return VBoxBuilder.create().add(dealersCardsDisplay, InterfaceUtils.createVerticalSpacer(), InterfaceUtils.createVerticalSpacer(), usersCardsDisplay,
                HBoxBuilder.create().add(valueLabel).centre().build(), HBoxBuilder.create().add(hitButton, standButton).spacing().padding().centre().build())
                .centre().styleClasses("green").build();
    }

    private ImageView createCardView(CardState cardState) {
        // Create the image view for the card and add it to the horizontal box containing the user's cards.
        ImageView cardImage = ImageViewBuilder.create().image(cardState.isHidden() ? Cards.CARD_BACK_TEXTURE : Cards.CARD_TEXTURES.get(cardState.getCard()))
                .height(160).preserveRatio().translateY(-this.scene.getHeight()).build();

        // Set up animations for the card.
        int nextIndex = this.timelines.size() + 1;
        this.timelines.add(TimelineBuilder.create().keyFrame(1500, cardImage.translateYProperty(), 0, Interpolator.EASE_BOTH)
                .onFinished(event -> this.playAnimation(nextIndex)).build());

        return cardImage;
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

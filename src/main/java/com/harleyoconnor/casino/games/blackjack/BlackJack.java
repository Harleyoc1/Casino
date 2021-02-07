package com.harleyoconnor.casino.games.blackjack;

import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.builders.HBoxBuilder;
import com.harleyoconnor.casino.builders.ImageViewBuilder;
import com.harleyoconnor.casino.builders.LabelBuilder;
import com.harleyoconnor.casino.builders.VBoxBuilder;
import com.harleyoconnor.casino.games.Game;
import com.harleyoconnor.casino.games.Games;
import com.harleyoconnor.casino.games.Player;
import com.harleyoconnor.casino.menus.MenuScreen;
import com.harleyoconnor.casino.textures.cards.Card;
import com.harleyoconnor.casino.textures.cards.Cards;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harley O'Connor
 */
public final class BlackJack extends Game {

    private List<Timeline> timelines;

    public BlackJack(Casino casino, Stage stage, Scene scene, MenuScreen previousScreen, Player player) {
        super(casino, stage, scene, previousScreen, player, Games.BLACKJACK);
    }

    @Override
    protected Pane setupScreen() {
        timelines = new ArrayList<>();

        List<Card> cards = new ArrayList<>(Cards.getRandomCards(2));

        HBoxBuilder<HBox> userCardsBuilder = HBoxBuilder.create();

        for (Card card : cards) {
            ImageView cardImage = ImageViewBuilder.create().image(Cards.CARD_TEXTURES.get(card)).height(160).preserveRatio().build();
            cardImage.translateYProperty().set(-this.scene.getHeight() / 2 - 160);
            KeyValue keyValue = new KeyValue(cardImage.translateYProperty(), 0, Interpolator.EASE_IN);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(900), keyValue);
            Timeline timeline = new Timeline(keyFrame);
            this.timelines.add(timeline);
            int nextIndex = this.timelines.size();
            timeline.setOnFinished(event -> {
                if (nextIndex >= this.timelines.size())
                    return;
                this.timelines.get(nextIndex).play();
            });
            userCardsBuilder.add(cardImage);
        }

        this.timelines.get(0).play();

        HBox usersCards = userCardsBuilder.spacing(10).padding().centre().build();
        Label valueLabel = LabelBuilder.create().text("Your Value: " + Cards.countCardsValues(cards)).styleClasses("white").body().build();

        return VBoxBuilder.create().add(usersCards, HBoxBuilder.create().add(valueLabel).centre().build()).centre().styleClasses("green").build();
    }

}

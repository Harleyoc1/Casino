package com.harleyoconnor.casino.games.blackjack;

import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.builders.HBoxBuilder;
import com.harleyoconnor.casino.builders.LabelBuilder;
import com.harleyoconnor.casino.games.Game;
import com.harleyoconnor.casino.games.Games;
import com.harleyoconnor.casino.games.Player;
import com.harleyoconnor.casino.menus.MenuScreen;
import com.harleyoconnor.casino.textures.cards.Cards;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public final class BlackJack extends Game {

    public BlackJack(Casino casino, Stage stage, Scene scene, MenuScreen previousScreen, Player player) {
        super(casino, stage, scene, previousScreen, player, Games.BLACKJACK);
    }

    @Override
    protected Pane setupScreen() {
        ImageView card = new ImageView(Cards.CARD_TEXTURES.get(Cards.CARDS.get(0)));
        card.setPreserveRatio(true);
        card.setFitWidth(100);

        return HBoxBuilder.createHBox().add(card, LabelBuilder.createLabel().text("Hello?").build()).centre().build();
    }

}

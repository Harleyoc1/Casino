package com.harleyoconnor.casino.builders;

import com.harleyoconnor.casino.utils.InterfaceUtils;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 * @author Harley O'Connor
 */
public final class HBoxBuilder<T extends HBox> extends PaneBuilder<T, HBoxBuilder<T>> {

    public HBoxBuilder(T hBox) {
        super(hBox);
    }

    public HBoxBuilder<T> centre () {
        ObservableList<Node> nodeList = this.node.getChildren();
        nodeList.add(0, InterfaceUtils.createHorizontalSpacer());
        nodeList.add(nodeList.size(), InterfaceUtils.createHorizontalSpacer());
        return this;
    }

    public static HBoxBuilder<HBox> createHBox () {
        return new HBoxBuilder<>(new HBox());
    }

}
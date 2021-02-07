package com.harleyoconnor.casino.builders;

import com.harleyoconnor.casino.utils.InterfaceUtils;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author Harley O'Connor
 */
public final class VBoxBuilder<T extends VBox> extends PaneBuilder<T, VBoxBuilder<T>> {

    public VBoxBuilder(T vBox) {
        super(vBox);
    }

    public VBoxBuilder<T> centre () {
        ObservableList<Node> nodeList = this.node.getChildren();
        nodeList.add(0, InterfaceUtils.createVerticalSpacer());
        nodeList.add(nodeList.size(), InterfaceUtils.createVerticalSpacer());
        return this;
    }

    public static VBoxBuilder<VBox> createVBox() {
        return new VBoxBuilder<>(new VBox());
    }

}

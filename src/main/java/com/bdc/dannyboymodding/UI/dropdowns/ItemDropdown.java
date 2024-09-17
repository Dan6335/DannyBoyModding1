package com.bdc.dannyboymodding.UI.dropdowns;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import static com.bdc.dannyboymodding.utils.AppUtils.styleDropdownButton;

public class ItemDropdown extends VBox {
    public ItemDropdown(boolean windowEnabled) {
        if (windowEnabled) {
            this.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY)));
            this.setBorder(new Border(new BorderStroke(Color.web("#4e4e4e"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            this.setPadding(new Insets(5));
            this.setSpacing(5);

            Button createItemModels = new Button("Create Item Models");
            Button createItemReg = new Button("Create Item Registries");

            styleDropdownButton(createItemModels);
            styleDropdownButton(createItemReg);

            this.getChildren().addAll(createItemModels, createItemReg);
            this.setMaxWidth(160);
            this.setMaxHeight(60);
            createItemModels.setMaxWidth(160);
            createItemReg.setMaxWidth(160);
        }
    }
}
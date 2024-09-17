package com.bdc.dannyboymodding.UI.dropdowns;

import com.bdc.dannyboymodding.utils.WindowUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import static com.bdc.dannyboymodding.utils.AppUtils.styleDropdownButton;

public class BlockDropdown extends VBox {
    public BlockDropdown(boolean pEnabled) {
        if (pEnabled) {
            this.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY)));
            this.setBorder(new Border(new BorderStroke(Color.web("#4e4e4e"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            this.setPadding(new Insets(5));
            this.setSpacing(5);

            Button createBlockModels = new Button("Create Block Models");
            Button createBlockReg = new Button("Create Block Registries");

            styleDropdownButton(createBlockModels);
            styleDropdownButton(createBlockReg);

            createBlockModels.setOnAction(e -> WindowUtils.showCreateBlockModels());

            this.getChildren().addAll(createBlockModels, createBlockReg);
            this.setMaxWidth(160);
            this.setMaxHeight(60);
            createBlockModels.setMaxWidth(160);
            createBlockReg.setMaxWidth(160);
        }
    }
}

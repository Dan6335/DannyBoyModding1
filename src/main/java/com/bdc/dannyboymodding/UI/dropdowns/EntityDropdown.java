package com.bdc.dannyboymodding.UI.dropdowns;

import com.bdc.dannyboymodding.utils.WindowUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import static com.bdc.dannyboymodding.utils.AppUtils.styleDropdownButton;

public class EntityDropdown extends VBox {
    public EntityDropdown(boolean windowEnabled) {
        if(windowEnabled) {
            this.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY)));
            this.setBorder(new Border(new BorderStroke(Color.web("#4e4e4e"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            this.setPadding(new Insets(5));
            this.setSpacing(5);

            Button createFromNames = new Button("Create From Names");
            Button createAttributes = new Button("Create Bdd Entity");

            styleDropdownButton(createFromNames);
            styleDropdownButton(createAttributes);

            createFromNames.setOnAction(e -> WindowUtils.showCreateFromNames());
            createAttributes.setOnAction(e -> WindowUtils.showCreateBddEntityReg());

            this.getChildren().addAll(createFromNames, createAttributes);
            this.setMaxWidth(160);
            createFromNames.setMaxWidth(160);
            createAttributes.setMaxWidth(160);
            this.setMaxHeight(60);}
    }
}
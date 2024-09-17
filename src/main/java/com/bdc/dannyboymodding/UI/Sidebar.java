package com.bdc.dannyboymodding.UI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import static com.bdc.dannyboymodding.utils.AppUtils.styleButton;
import static com.bdc.dannyboymodding.utils.AppUtils.toggleDropdown;

public class Sidebar extends VBox {
    public Sidebar() {
        this.setPrefWidth(180);
        this.setBackground(new Background(new BackgroundFill(Color.web("#2e2e2e"), CornerRadii.EMPTY, Insets.EMPTY)));
        this.setPadding(new Insets(10, 0, 0, 0));
        this.setSpacing(5);

        Button createEntityButton = new Button("Create Entity");
        Button createItemButton = new Button("Create Item");
        Button createBlockButton = new Button("Create Block");

        styleButton(createEntityButton);
        styleButton(createItemButton);
        styleButton(createBlockButton);

        createEntityButton.setOnAction(e -> toggleDropdown("entityDropdown"));
        createItemButton.setOnAction(e -> toggleDropdown("itemDropdown"));
        createBlockButton.setOnAction(e -> toggleDropdown("blockDropdown"));

        this.getChildren().addAll(createEntityButton, createItemButton, createBlockButton);
    }
}
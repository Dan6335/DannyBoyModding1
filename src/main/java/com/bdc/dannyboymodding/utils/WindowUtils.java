package com.bdc.dannyboymodding.utils;

import com.bdc.dannyboymodding.windows.CreateBlockModels;
import com.bdc.dannyboymodding.windows.CreateEntityReg;
import com.bdc.dannyboymodding.windows.CreateFromNames;
import com.bdc.dannyboymodding.windows.CreateItemModels;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import static com.bdc.dannyboymodding.utils.AppUtils.primaryStage;

public class WindowUtils {
    private static void showContent(VBox pContent) {
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
        StackPane dropdownContainer = (StackPane) root.getCenter();
        dropdownContainer.getChildren().clear();
        dropdownContainer.getChildren().add(pContent);
    }

    public static void showCreateFromNames() {
        showContent(new CreateFromNames());
    }

    public static void showCreateBddEntityReg() {
        showContent(new CreateEntityReg());
    }

    public static void showCreateItemModels() {
        showContent(new CreateItemModels());
    }

    public static void showCreateBlockModels() {
        showContent(new CreateBlockModels());
    }
}
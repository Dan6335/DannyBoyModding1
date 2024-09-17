package com.bdc.dannyboymodding.utils;

import com.bdc.dannyboymodding.windows.CreateEntityReg;
import com.bdc.dannyboymodding.windows.CreateFromNames;
import com.bdc.dannyboymodding.windows.CreateItemModels;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import static com.bdc.dannyboymodding.utils.AppUtils.primaryStage;

public class WindowUtils {
    public static void showCreateFromNames() {
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
        StackPane dropdownContainer = (StackPane) root.getCenter();
        VBox content = new CreateFromNames();
        dropdownContainer.getChildren().clear();
        dropdownContainer.getChildren().add(content);
    }

    public static void showCreateBddEntityReg() {
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
        StackPane dropdownContainer = (StackPane) root.getCenter();
        VBox content = new CreateEntityReg();
        dropdownContainer.getChildren().clear();
        dropdownContainer.getChildren().add(content);
    }

    public static void showCreateItemModels() {
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
        StackPane dropdownContainer = (StackPane) root.getCenter();
        VBox content = new CreateItemModels();
        dropdownContainer.getChildren().clear();
        dropdownContainer.getChildren().add(content);
    }
}
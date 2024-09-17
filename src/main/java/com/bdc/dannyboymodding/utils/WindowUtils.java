package com.bdc.dannyboymodding.utils;

import com.bdc.dannyboymodding.windows.CreateEntityReg;
import com.bdc.dannyboymodding.windows.CreateFromNames;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import static com.bdc.dannyboymodding.utils.AppUtils.*;

public class WindowUtils {
    public static void showCreateFromNames() {
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
        StackPane dropdownContainer = (StackPane) root.getCenter();
        CreateFromNames createFromNames = new CreateFromNames();
        VBox content = createFromNames.getView();
        dropdownContainer.getChildren().clear();
        dropdownContainer.getChildren().add(content);
    }

    public static void showCreateBddEntityReg() {
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
        StackPane dropdownContainer = (StackPane) root.getCenter();
        CreateEntityReg createFromNames = new CreateEntityReg();
        VBox content = createFromNames.getView();
        dropdownContainer.getChildren().clear();
        dropdownContainer.getChildren().add(content);
    }
}
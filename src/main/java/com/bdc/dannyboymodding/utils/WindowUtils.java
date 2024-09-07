package com.bdc.dannyboymodding.utils;

import com.bdc.dannyboymodding.Main;
import com.bdc.dannyboymodding.windows.CreateAttributes;
import com.bdc.dannyboymodding.windows.CreateFromNames;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class WindowUtils {
    public static void showCreateFromNames() {
        BorderPane root = (BorderPane) Main.primaryStage.getScene().getRoot();
        StackPane dropdownContainer = (StackPane) root.getCenter();

        // Use the CreateFromNames class to generate the content
        CreateFromNames createFromNames = new CreateFromNames();
        VBox content = createFromNames.getView();

        // Clear the previous content and add the new one
        dropdownContainer.getChildren().clear();
        dropdownContainer.getChildren().add(content);
    }

    public static void showCreateBddEntityReg() {
        BorderPane root = (BorderPane) Main.primaryStage.getScene().getRoot();
        StackPane dropdownContainer = (StackPane) root.getCenter();

        // Use the CreateFromNames class to generate the content
        CreateAttributes createFromNames = new CreateAttributes();
        VBox content = createFromNames.getView();

        // Clear the previous content and add the new one
        dropdownContainer.getChildren().clear();
        dropdownContainer.getChildren().add(content);
    }
}

package com.bdc.dannyboymodding.utils;

import com.bdc.dannyboymodding.UI.dropdowns.BlockDropdown;
import com.bdc.dannyboymodding.UI.dropdowns.EntityDropdown;
import com.bdc.dannyboymodding.UI.dropdowns.ItemDropdown;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AppUtils {
    public static Label outputPathLable = new Label("Select output folder");
    public static Stage primaryStage;
    public static String codesDir = "codes/";
    public static List<String> selectedClassNames = new ArrayList<>();


    public static void openDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Folder");
        Window stage = outputPathLable.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            outputPathLable.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public static void styleDropdownButton(Button button) {
        button.setPrefWidth(120);
        button.setPrefHeight(25);
        button.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY)));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-font-size: 12px; -fx-cursor: hand; -fx-alignment: center-left;");
        button.setOnMouseEntered(e -> button.setBackground(new Background(new BackgroundFill(Color.web("#4e4e4e"), CornerRadii.EMPTY, Insets.EMPTY))));
        button.setOnMouseExited(e -> button.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY))));
    }

    public static void styleButton(Button button) {
        button.setPrefWidth(160);
        button.setPrefHeight(35);
        button.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY)));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-font-size: 14px; -fx-cursor: hand; -fx-alignment: center-left;"); // Further reduced font size
        button.setOnMouseEntered(e -> button.setBackground(new Background(new BackgroundFill(Color.web("#4e4e4e"), CornerRadii.EMPTY, Insets.EMPTY))));
        button.setOnMouseExited(e -> button.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY))));
    }

    public static void toggleDropdown(String dropdownId) {
        VBox dropdown = null;
        Insets margin = switch (dropdownId) {
            case "entityDropdown" -> {
                dropdown = new EntityDropdown(true);
                yield new Insets(-10, 0, 0, -15);
            }
            case "itemDropdown" -> {
                dropdown = new ItemDropdown(true);
                yield new Insets(30, 0, 0, -15);
            }
            case "blockDropdown" -> {
                dropdown = new BlockDropdown(true);
                yield new Insets(60, 0, 0, -15);
            }
            default -> null;
        };

        if (dropdown != null) {
            BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
            StackPane dropdownContainer = (StackPane) root.getCenter();
            dropdownContainer.getChildren().removeIf(node -> node instanceof VBox && node != dropdownContainer.getChildren().get(0));
            dropdownContainer.getChildren().add(dropdown);
            StackPane.setAlignment(dropdown, Pos.TOP_LEFT);
            StackPane.setMargin(dropdown, margin);
        }
    }

    public static String loadResourceFile(String pFileName) {
        InputStream inputStream = AppUtils.class.getClassLoader().getResourceAsStream(codesDir + pFileName);
        if (inputStream == null) {
            System.err.println("Resource file not found: " + pFileName);
            showAlert(Alert.AlertType.ERROR, "Error", "Resource file not found: " + pFileName);
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to read resource file: " + pFileName);
            return null;
        }
    }

    public static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
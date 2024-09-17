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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AppUtils {
    public static Label outputPathLabel = new Label("Select output folder");
    public static Stage primaryStage;
    public static String codesDir = "codes/";
    public static List<String> selectedClassNames = new ArrayList<>();

    public static void openDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Folder");
        Window stage = outputPathLabel.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            outputPathLabel.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public static void styleDropdownButton(Button pButton) {
        pButton.setPrefWidth(120);
        pButton.setPrefHeight(25);
        pButton.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY)));
        pButton.setTextFill(Color.WHITE);
        pButton.setStyle("-fx-font-size: 12px; -fx-cursor: hand; -fx-alignment: center-left;");
        pButton.setOnMouseEntered(e -> pButton.setBackground(new Background(new BackgroundFill(Color.web("#4e4e4e"), CornerRadii.EMPTY, Insets.EMPTY))));
        pButton.setOnMouseExited(e -> pButton.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY))));
    }

    public static void styleButton(Button pButton) {
        pButton.setPrefWidth(160);
        pButton.setPrefHeight(35);
        pButton.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY)));
        pButton.setTextFill(Color.WHITE);
        pButton.setStyle("-fx-font-size: 14px; -fx-cursor: hand; -fx-alignment: center-left;"); // Further reduced font size
        pButton.setOnMouseEntered(e -> pButton.setBackground(new Background(new BackgroundFill(Color.web("#4e4e4e"), CornerRadii.EMPTY, Insets.EMPTY))));
        pButton.setOnMouseExited(e -> pButton.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY))));
    }

    public static void toggleDropdown(String pDropdown) {
        VBox dropdown = null;
        Insets margin = switch (pDropdown) {
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

    public static void showAlert(Alert.AlertType pType, String pTitle, String pMessage) {
        Alert alert = new Alert(pType, pMessage, ButtonType.OK);
        alert.setTitle(pTitle);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static String toPascalCase(String pName) {
        return Arrays.stream(pName.split("\\s+"))
                .map(part -> Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase())
                .collect(Collectors.joining());
    }

    public static String toLowerCaseWithUnderscores(String pInput) {
        return pInput.trim().toLowerCase().replaceAll("\\s+", "_");
    }

    public static void writeToFile(File file, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException eException) {
            eException.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to write file: " + file.getAbsolutePath());
        }
    }
}
package com.bdc.dannyboymodding.windows;

import com.bdc.dannyboymodding.UI.buttons.BrowseButton;
import com.bdc.dannyboymodding.UI.buttons.OutputPathBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.bdc.dannyboymodding.utils.AppUtils.*;

public class CreateFromNames extends VBox{
    private final TextField nameField;
    private final ComboBox<String> entityTypeComboBox;

    public CreateFromNames() {
        this.setPadding(new Insets(20));
        this.setSpacing(30); // Space between elements
        this.setStyle("-fx-background-color: #1c1c1c;");
        this.setAlignment(Pos.TOP_CENTER); // Align everything to the top-center
        Text title = new Text("Create From Names:");
        title.setFill(Color.web("#1e90ff"));
        title.setStyle("-fx-font-size: 24px;");
        VBox topSection = new VBox();
        topSection.setSpacing(10);
        topSection.setAlignment(Pos.TOP_CENTER);
        topSection.getChildren().addAll(title);
        VBox.setMargin(topSection, new Insets(100, 0, 0, 0));

        HBox dropdownBox = new HBox();
        dropdownBox.setSpacing(10);
        dropdownBox.setAlignment(Pos.CENTER);
        Text dropdownLabel = new Text("Select Entity Type:");
        dropdownLabel.setFill(Color.WHITE);
        dropdownLabel.setStyle("-fx-font-size: 14px;");
        entityTypeComboBox = new ComboBox<>();
        entityTypeComboBox.getItems().addAll("JurassiCraft Dinosaur", "Bluedude Dragons Dragon", "Custom Entity(WIP)");
        entityTypeComboBox.setPrefWidth(300);
        dropdownBox.getChildren().addAll(dropdownLabel, entityTypeComboBox);
        VBox.setMargin(dropdownBox, new Insets(-10, 0, 0, 0));

        HBox inputBox = new HBox();
        inputBox.setSpacing(4);
        inputBox.setAlignment(Pos.CENTER);
        VBox.setMargin(inputBox, new Insets(-10, 0, 0, -52));
        Text label = new Text("Names (comma-separated):");
        label.setFill(Color.WHITE);
        label.setStyle("-fx-font-size: 14px;");
        nameField = new TextField();
        nameField.setPromptText("Enter names here");
        nameField.setPrefWidth(300);
        inputBox.getChildren().addAll(label, nameField);

        OutputPathBox outputPathBox = new OutputPathBox(-10, -43, 0, 0, 4);

        Text outputPathLabelText = new Text("Output Path:");
        outputPathLabelText.setFill(Color.WHITE);
        outputPathLabelText.setStyle("-fx-font-size: 14px;");

        outputPathLable.setText("Select output folder");
        outputPathLable.setStyle("-fx-background-color: #2e2e2e; -fx-text-fill: white;");
        outputPathLable.setPrefWidth(235);
        outputPathLable.setPrefHeight(20);
        outputPathLable.setAlignment(Pos.CENTER);

        BrowseButton browseButton = new BrowseButton("Browse...");

        outputPathBox.getChildren().addAll(outputPathLabelText, outputPathLable, browseButton);

        Button generateClassesButton = new Button("Generate Classes");
        generateClassesButton.setPrefWidth(300);
        generateClassesButton.setOnAction(e -> generateClasses());
        generateClassesButton.setStyle("-fx-background-color: #3788ca; -fx-text-fill: white;");
        this.getChildren().addAll(topSection, dropdownBox, inputBox, outputPathBox, generateClassesButton);
    }

    private void generateClasses() {
        String selectedEntityType = entityTypeComboBox.getValue();
        String outputPath = outputPathLable.getText();
        if (outputPath == null || outputPath.isEmpty() || "Select output folder".equals(outputPath)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a valid output folder.");
            return;
        }

        switch (selectedEntityType) {
            case "JurassiCraft Dinosaur":
                generateJurassiCraftClasses();
                break;
            case "Bluedude Dragons Dragon":
                showBluedudeDragonsOptionsDialog();
                break;
            case "Custom Entity":
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a valid entity type.");
                break;
        }
    }

    private String toPascalCase(String name) {
        return Arrays.stream(name.split("\\s+"))
                .map(part -> Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase())
                .collect(Collectors.joining());
    }

    private void generateJurassiCraftClasses() {
        String fileContent = loadResourceFile("JurassiCraftDinoCode.txt");
        List<String> names = Arrays.stream(nameField.getText().split(",")).map(String::trim).map(this::toPascalCase).toList();

        if (fileContent != null) {
            for (String name : names) {
                String fileContentWithName = fileContent.replace("{class_name}", name);
                File outputFile = new File(outputPathLable.getText(), name + ".java");

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                    writer.write(fileContentWithName);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to write file: " + outputFile.getAbsolutePath());
                }
            }
        }
    }

    private void generateBluedudeDragonsClasses(boolean projDragon, boolean tamableDragon, boolean ridableDragon, boolean breathDragon, boolean undergroundDragon, boolean fearWater) {
        String fileContent = loadResourceFile("BluedudeDragonsDragon.txt");
        List<String> names = Arrays.stream(nameField.getText().split(",")).map(String::trim).map(this::toPascalCase).toList();
        if (fileContent != null) {
            List<String> interfaces = new ArrayList<>();
            if (projDragon) interfaces.add("IProjDragon");
            if (tamableDragon) interfaces.add("IDragonTamable");
            if (ridableDragon) interfaces.add("IRidableDragon");
            if (breathDragon) interfaces.add("IBreathDragon");
            if (undergroundDragon) interfaces.add("IUndergroundDragon");
            if (fearWater) interfaces.add("IFearWater");

            String interfacesString = String.join(", ", interfaces);
            fileContent = fileContent.replace("implements {interface}", interfacesString.isEmpty() ? "" : "implements " + interfacesString);
            for (String name : names) {
                String fileContentWithName = fileContent.replace("{class_name}", name);
                File outputFile = new File(outputPathLable.getText(), name + ".java");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                    writer.write(fileContentWithName);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to write file: " + outputFile.getAbsolutePath());
                }
            }
        }
    }

    private void showBluedudeDragonsOptionsDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Bluedude Dragons Options");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        CheckBox projDragonCheckBox = new CheckBox("Proj Dragon");
        CheckBox tamableDragonCheckBox = new CheckBox("Tamable Dragon");
        CheckBox ridableDragonCheckBox = new CheckBox("Ridable Dragon");
        CheckBox breathDragonCheckBox = new CheckBox("Breath Dragon");
        CheckBox undergroundDragonCheckBox = new CheckBox("Underground Dragon");
        CheckBox waterFearingDragonCheckBox = new CheckBox("Water Fearing Dragon");
        vbox.getChildren().addAll(projDragonCheckBox, tamableDragonCheckBox, ridableDragonCheckBox, breathDragonCheckBox);

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            generateBluedudeDragonsClasses(projDragonCheckBox.isSelected(), tamableDragonCheckBox.isSelected(),
                    ridableDragonCheckBox.isSelected(), breathDragonCheckBox.isSelected(),
                    undergroundDragonCheckBox.isSelected(), waterFearingDragonCheckBox.isSelected());
            dialog.close();
        });

        vbox.getChildren().add(okButton);
        Scene scene = new Scene(vbox);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}
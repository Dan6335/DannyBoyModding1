package com.bdc.dannyboymodding.windows;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreateFromNames {
    private Label outputPathLabel;
    private TextField nameField;
    private ComboBox<String> entityTypeComboBox;
    public String codesDir = "com/bdc/dannyboymodding/codes/";

    public VBox getView() {
        // Main VBox container
        VBox content = new VBox();
        content.setPadding(new Insets(20));
        content.setSpacing(30); // Space between elements
        content.setStyle("-fx-background-color: #1c1c1c;");
        content.setAlignment(Pos.TOP_CENTER); // Align everything to the top-center

        // Add a title text
        Text title = new Text("Create From Names:");
        title.setFill(Color.web("#1e90ff"));
        title.setStyle("-fx-font-size: 24px;");

        // VBox for title and description at the top
        VBox topSection = new VBox();
        topSection.setSpacing(10);
        topSection.setAlignment(Pos.TOP_CENTER);
        topSection.getChildren().addAll(title);
        VBox.setMargin(topSection, new Insets(100, 0, 0, 0)); // Margin: top, right, bottom, left

        // Create a dropdown (ComboBox) for selecting entity type
        HBox dropdownBox = new HBox();
        dropdownBox.setSpacing(10);
        dropdownBox.setAlignment(Pos.CENTER); // Center the dropdown box horizontally

        // Label for the dropdown
        Text dropdownLabel = new Text("Select Entity Type:");
        dropdownLabel.setFill(Color.WHITE);
        dropdownLabel.setStyle("-fx-font-size: 14px;");

        // ComboBox for selecting entity types
        entityTypeComboBox = new ComboBox<>();
        entityTypeComboBox.getItems().addAll("JurassiCraft Dinosaur", "Bluedude Dragons Dragon", "Custom Entity(WIP)");
        entityTypeComboBox.setPrefWidth(300); // Set a preferred width for the dropdown

        // Add label and ComboBox to the HBox
        dropdownBox.getChildren().addAll(dropdownLabel, entityTypeComboBox);

        // Add a margin to the dropdownBox to move it lower
        VBox.setMargin(dropdownBox, new Insets(-10, 0, 0, 0)); // Margin: top, right, bottom, left

        // Add a label and text field in an HBox, centered horizontally
        HBox inputBox = new HBox();
        inputBox.setSpacing(4);
        inputBox.setAlignment(Pos.CENTER); // Center the input box horizontally
        VBox.setMargin(inputBox, new Insets(-10, 0, 0, -52)); // Margin: top, right, bottom, left

        // Label for text field
        Text label = new Text("Names (comma-separated):");
        label.setFill(Color.WHITE);
        label.setStyle("-fx-font-size: 14px;");

        // TextField for entering names
        nameField = new TextField();
        nameField.setPromptText("Enter names here");
        nameField.setPrefWidth(300); // Set a preferred width for the text field

        // Add label and text field to the HBox
        inputBox.getChildren().addAll(label, nameField);

        // Create a label, and button for output path
        HBox outputPathBox = new HBox();
        outputPathBox.setSpacing(4);
        outputPathBox.setAlignment(Pos.CENTER); // Center the output path box horizontally
        VBox.setMargin(outputPathBox, new Insets(-10, -43, 0, 0)); // Margin: top, right, bottom, left

        // Label for output path
        Text outputPathLabelText = new Text("Output Path:");
        outputPathLabelText.setFill(Color.WHITE);
        outputPathLabelText.setStyle("-fx-font-size: 14px;");

        // Label to display output path
        outputPathLabel = new Label();
        outputPathLabel.setText("Select output folder");
        outputPathLabel.setPrefWidth(235); // Set a preferred width for the label
        outputPathLabel.setPrefHeight(20); // Set a preferred height for the label
        outputPathLabel.setStyle("-fx-background-color: #2e2e2e; -fx-text-fill: white;"); // Style to look like a non-editable text field

        // Browse button to open file chooser dialog
        Button browseButton = new Button("Browse...");
        browseButton.setOnAction(e -> openDirectoryChooser());

        // Add label, and button to the HBox
        outputPathBox.getChildren().addAll(outputPathLabelText, outputPathLabel, browseButton);

        // Create a "Generate Classes" button
        Button generateClassesButton = new Button("Generate Classes");
        generateClassesButton.setOnAction(e -> generateClasses());

        // Add the top section (title), dropdown, input box, output path box, and generate button to the main VBox
        content.getChildren().addAll(topSection, dropdownBox, inputBox, outputPathBox, generateClassesButton);

        return content;
    }

    private void openDirectoryChooser() {
        // Create a DirectoryChooser
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Folder");

        // Show the dialog and get the selected directory
        Window stage = outputPathLabel.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);

        // Set the path to the label if a directory is selected
        if (selectedDirectory != null) {
            outputPathLabel.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void generateClasses() {
        // Get the input names and convert them to PascalCase
        String inputNames = nameField.getText();
        String[] namesArray = inputNames.split("\\s*,\\s*");
        List<String> pascalCaseNames = Arrays.stream(namesArray)
                .map(this::toPascalCase)
                .toList();

        // Get the selected entity type
        String selectedEntityType = entityTypeComboBox.getValue();

        // Validate output path
        String outputPath = outputPathLabel.getText();
        if (outputPath == null || outputPath.isEmpty() || "Select output folder".equals(outputPath)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a valid output folder.");
            return;
        }

        // Generate classes based on the selected entity type
        switch (selectedEntityType) {
            case "JurassiCraft Dinosaur":
                generateJurassiCraftClasses();
                break;
            case "Bluedude Dragons Dragon":
                showBluedudeDragonsOptionsDialog();
                break;
            case "Custom Entity":
                // Handle Custom Entity class generation
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a valid entity type.");
                break;
        }
    }

    private String toPascalCase(String name) {
        // Convert a string to PascalCase
        return Arrays.stream(name.split("\\s+"))
                .map(part -> Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase())
                .collect(Collectors.joining());
    }

    private void generateJurassiCraftClasses() {
        // Convert names to PascalCase
        List<String> names = Arrays.stream(nameField.getText().split(","))
                .map(String::trim)
                .map(this::toPascalCase)
                .toList();

        // Get the content from the resource file
        String fileContent = loadResourceFile("JurassiCraftDinoCode.txt");

        if (fileContent != null) {
            // Create files for each name with the content from the resource file
            for (String name : names) {
                String fileContentWithName = fileContent.replace("{class_name}", name);
                File outputFile = new File(outputPathLabel.getText(), name + ".java");

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                    writer.write(fileContentWithName);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to write file: " + outputFile.getAbsolutePath());
                }
            }
        }
    }

    private void generateBluedudeDragonsClasses(boolean projDragon, boolean tamableDragon, boolean ridableDragon, boolean breathDragon,
                                                boolean undergroundDragon, boolean fearWater) {
        // Convert names to PascalCase
        List<String> names = Arrays.stream(nameField.getText().split(","))
                .map(String::trim)
                .map(this::toPascalCase)
                .toList();

        // Get the content from the resource file
        String fileContent = loadResourceFile("BluedudeDragonsDragon.txt");

        if (fileContent != null) {
            // Determine the interfaces to implement
            List<String> interfaces = new ArrayList<>();
            if (projDragon) interfaces.add("ProjDragon");
            if (tamableDragon) interfaces.add("TamableDragon");
            if (ridableDragon) interfaces.add("RidableDragon");
            if (breathDragon) interfaces.add("BreathDragon");
            if (undergroundDragon) interfaces.add("IUndergroundDragon");
            if (fearWater) interfaces.add("IFearWater");

            String interfacesString = String.join(", ", interfaces);
            fileContent = fileContent.replace("implements {interface}", interfacesString.isEmpty() ? "" : "implements " + interfacesString);

            // Create files for each name with the content from the resource file
            for (String name : names) {
                String fileContentWithName = fileContent.replace("{class_name}", name);
                File outputFile = new File(outputPathLabel.getText(), name + ".java");

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
        // Create a new Stage (window)
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Bluedude Dragons Options");

        // Create a VBox to hold the checkboxes and OK button
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        // Create checkboxes
        CheckBox projDragonCheckBox = new CheckBox("Proj Dragon");
        CheckBox tamableDragonCheckBox = new CheckBox("Tamable Dragon");
        CheckBox ridableDragonCheckBox = new CheckBox("Ridable Dragon");
        CheckBox breathDragonCheckBox = new CheckBox("Breath Dragon");
        CheckBox undergroundDragonCheckBox = new CheckBox("Underground Dragon");
        CheckBox waterFearingDragonCheckBox = new CheckBox("Water Fearing Dragon");

        // Add checkboxes to VBox
        vbox.getChildren().addAll(projDragonCheckBox, tamableDragonCheckBox, ridableDragonCheckBox, breathDragonCheckBox);

        // Create OK button
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            generateBluedudeDragonsClasses(projDragonCheckBox.isSelected(), tamableDragonCheckBox.isSelected(),
                    ridableDragonCheckBox.isSelected(), breathDragonCheckBox.isSelected(),
                    undergroundDragonCheckBox.isSelected(), waterFearingDragonCheckBox.isSelected());
            dialog.close();
        });

        // Add OK button to VBox
        vbox.getChildren().add(okButton);

        // Set the scene and show the dialog
        Scene scene = new Scene(vbox);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private String loadResourceFile(String fileName) {
        // Ensure that the fileName includes the correct path relative to the resources directory
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(codesDir + fileName);

        if (inputStream == null) {
            // Print an error message to the console for debugging purposes
            System.err.println("Resource file not found: " + fileName);
            showAlert(Alert.AlertType.ERROR, "Error", "Resource file not found: " + fileName);
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            // Print the stack trace for debugging purposes
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to read resource file: " + fileName);
            return null;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
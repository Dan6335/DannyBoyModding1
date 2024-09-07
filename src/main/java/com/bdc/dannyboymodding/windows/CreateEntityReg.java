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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreateEntityReg {
    public String codesDir = "com/bdc/dannyboymodding/codes/";
    private Label outputPathLabel;
    private List<String> selectedClassNames = new ArrayList<>();

    public VBox getView() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);

        Text title = new Text("Create Bluedude Dragons Dragon Registry:");
        title.setFill(Color.web("#1e90ff"));
        title.setStyle("-fx-font-size: 24px;");

        // File selection button
        Button selectFilesButton = new Button("Select .java Files");
        selectFilesButton.setStyle("-fx-background-color: #3788ca; -fx-text-fill: white;");
        selectFilesButton.setPrefWidth(300);
        selectFilesButton.setOnAction(e -> openFileChooser());

        // Output path selection
        Text outputPathLabelText = new Text("Output Path:");
        outputPathLabelText.setFill(Color.WHITE);
        outputPathLabelText.setStyle("-fx-font-size: 14px;");

        outputPathLabel = new Label("Select output folder");
        outputPathLabel.setStyle("-fx-background-color: #2e2e2e; -fx-text-fill: white;");

        Button browseButton = new Button("Browse...");
        browseButton.setOnAction(e -> openDirectoryChooser());

        HBox outputPathBox = new HBox();
        outputPathBox.setSpacing(4);
        outputPathBox.setAlignment(Pos.CENTER); // Center the output path box horizontally
        //VBox.setMargin(outputPathBox, new Insets(-10, 0, 0, 0)); // Margin: top, right, bottom, left

        //VBox outputPathBox = new VBox(10, outputPathLabelText, outputPathLabel, browseButton);
        outputPathBox.getChildren().addAll(outputPathLabelText, outputPathLabel, browseButton);

        // Generate button
        Button generateButton = new Button("Generate EntityReg.txt");
        generateButton.setOnAction(e -> generateEntityRegFile());
        generateButton.setPrefWidth(300);
        generateButton.setStyle("-fx-background-color: #3788ca; -fx-text-fill: white;");

        content.getChildren().addAll(title, selectFilesButton, outputPathBox, generateButton);
        return content;
    }

    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select .java Files");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Files", "*.java"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        if (selectedFiles != null) {
            showCheckboxDialog(selectedFiles);
        }
    }

    private void showCheckboxDialog(List<File> files) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Select Entries");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        List<CheckBox> checkBoxes = new ArrayList<>();
        for (File file : files) {
            String fileName = file.getName();
            String className = fileName.replace(".java", "");
            CheckBox checkBox = new CheckBox(className);
            checkBoxes.add(checkBox);
            vbox.getChildren().add(checkBox);
        }

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            selectedClassNames = checkBoxes.stream()
                    .filter(CheckBox::isSelected)
                    .map(CheckBox::getText)
                    .collect(Collectors.toList());
            dialog.close();
        });

        vbox.getChildren().add(okButton);

        Scene scene = new Scene(vbox);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void openDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Folder");

        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            outputPathLabel.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void generateEntityRegFile() {
        String outputPath = outputPathLabel.getText();
        if (outputPath == null || outputPath.isEmpty() || "Select output folder".equals(outputPath)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a valid output folder.");
            return;
        }

        if (selectedClassNames.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No classes selected.");
            return;
        }

        String entityRegCode = loadResourceFile("BluedudeDragonsReg.txt");
        if (entityRegCode == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to get entity registration code.");
            return;
        }

        File outputFile = new File(outputPath, "EntityReg.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String className : selectedClassNames) {
                String lowerCaseName = className.toLowerCase().replace("entity", "");
                String upperCaseName = className.toUpperCase().replace("ENTITY", "");
                String renderName = className.replace("Entity", "");

                String content = entityRegCode
                        .replace("{lowercase_name}", lowerCaseName)
                        .replace("{class_name}", className)
                        .replace("{uppercase_name}", upperCaseName)
                        .replace("{render_name}", renderName);

                writer.write(content);
                writer.newLine(); // First line break
                writer.newLine(); // Second line break
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to write file: " + outputFile.getAbsolutePath());
        }
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
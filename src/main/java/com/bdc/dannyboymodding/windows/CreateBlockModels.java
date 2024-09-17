package com.bdc.dannyboymodding.windows;

import com.bdc.dannyboymodding.UI.buttons.BrowseButton;
import com.bdc.dannyboymodding.UI.buttons.OutputPathBox;
import com.bdc.dannyboymodding.utils.AppUtils;
import com.bdc.dannyboymodding.utils.NewInsets;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.bdc.dannyboymodding.utils.AppUtils.*;

public class CreateBlockModels extends VBox {
    private final TextField nameField;
    private final TextField modIdField;
    private final ComboBox<String> entityTypeComboBox;
    public CreateBlockModels() {
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);

        Text title = new Text("Create Block Models:");
        title.setFill(Color.web("#1e90ff"));
        title.setStyle("-fx-font-size: 24px;");

        VBox topSection = new VBox();
        topSection.setSpacing(90);
        topSection.setAlignment(Pos.TOP_CENTER);
        topSection.getChildren().addAll(title);
        VBox.setMargin(topSection, new Insets(-50, 0, 0, 0));

        HBox dropdownBox = new HBox();
        dropdownBox.setSpacing(10);
        dropdownBox.setAlignment(Pos.CENTER);
        Text dropdownLabel = new Text("Select Block Type:");
        dropdownLabel.setFill(Color.WHITE);
        dropdownLabel.setStyle("-fx-font-size: 14px;");
        entityTypeComboBox = new ComboBox<>();
        entityTypeComboBox.getItems().addAll("Normal Block", "Grass Block", "Flower Block", "Fence Gate Block", "Fence Block", "Slab Block",
                "Log Block", "Stairs Block");
        entityTypeComboBox.setPrefWidth(300);
        dropdownBox.getChildren().addAll(dropdownLabel, entityTypeComboBox);
        VBox.setMargin(dropdownBox, new NewInsets(-10, 0, 0, 0));

        HBox namesBox = new HBox();
        namesBox.setSpacing(4);
        namesBox.setAlignment(Pos.CENTER);
        VBox.setMargin(namesBox, new Insets(-10, 0, 0, -52));
        Text label = new Text("Names (comma-separated):");
        label.setFill(Color.WHITE);
        label.setStyle("-fx-font-size: 14px;");
        nameField = new TextField();
        nameField.setPromptText("Enter names here");
        nameField.setPrefWidth(300);
        namesBox.getChildren().addAll(label, nameField);

        HBox modIdBox = new HBox();
        modIdBox.setSpacing(4);
        modIdBox.setAlignment(Pos.CENTER);
        VBox.setMargin(modIdBox, new Insets(-10, 0, 0, -52));
        Text modIdlabel = new Text("Mod ID:");
        modIdlabel.setFill(Color.WHITE);
        modIdlabel.setStyle("-fx-font-size: 14px;");
        modIdField = new TextField();
        modIdField.setPromptText("Enter Mod ID");
        modIdField.setPrefWidth(100);
        modIdBox.getChildren().addAll(modIdlabel, modIdField);

        Text outputPathLabelText = new Text("Output Path:");
        outputPathLabelText.setFill(Color.WHITE);
        outputPathLabelText.setStyle("-fx-font-size: 14px;");

        outputPathLable.setText("Select output folder");
        outputPathLable.setStyle("-fx-background-color: #2e2e2e; -fx-text-fill: white;");
        outputPathLable.setPrefWidth(225);
        outputPathLable.setPrefHeight(20);
        outputPathLable.setAlignment(Pos.CENTER);

        Button generateButton = new Button("Generate Item Models");
        generateButton.setOnAction(e -> generateBlockModels());
        generateButton.setPrefWidth(300);
        generateButton.setStyle("-fx-background-color: #3788ca; -fx-text-fill: white;");

        BrowseButton browseButton = new BrowseButton("Browse...");
        OutputPathBox outputPathBox = new OutputPathBox(-10, -43, 0, 0, 4);
        outputPathBox.getChildren().addAll(outputPathLabelText, outputPathLable, browseButton);

        VBox.setMargin(namesBox, new NewInsets(10, 0, 0, -130));
        VBox.setMargin(modIdBox, new NewInsets(10, 0, 0, -209));
        VBox.setMargin(outputPathBox, new NewInsets(10, 0, 0, -45));
        VBox.setMargin(generateButton, new NewInsets(10, 0, 0, 0));
        VBox.setMargin(dropdownBox, new NewInsets(10, 0, 0, -75));

        this.getChildren().addAll(topSection, dropdownBox, namesBox, modIdBox, outputPathBox, generateButton);
    }

    private void generateBlockModels() {
        String selectedEntityType = entityTypeComboBox.getValue();
        String outputPath = outputPathLable.getText();
        if (outputPath == null || outputPath.isEmpty() || "Select output folder".equals(outputPath)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a valid output folder.");
            return;
        }

        switch (selectedEntityType) {
            case "Normal Block":
                generateBasicBlockModel();
                break;
            case "Grass Block":
                generateGrassBlockModel();
                break;
            case "Flower Block":
                break;
            case "Fence Gate Block":
                generateFenceGateBlockModels();
                break;
            case "Fence Block":
                generateFenceBlockModels();
                break;
            case "Slab Block":
                generateSlabBlockModels();
                break;
            case "Log Block":
                generateLogBlockModels();
                break;
            case "Stairs Block":
                generateStairBlockModels();
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a valid entity type.");
                break;
        }

        // Show success alert with OK button
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Successfully Generated Entity Class.");

        // When OK is pressed, reset the fields
        successAlert.setOnHidden(evt -> {
            outputPathLable.setText("Select output folder");
            nameField.clear();
            entityTypeComboBox.setValue(null);
        });

        successAlert.showAndWait();
    }

    private void generateBasicBlockModel() {
        String fileContent = loadResourceFile("blocks/BasicBlockCode.txt");
        List<String> names = Arrays.stream(nameField.getText().split(",")).map(AppUtils::toLowerCaseWithUnderscores).map(String::trim).toList();
        List<String> modIDF = Arrays.stream(modIdField.getText().split(",")).map(AppUtils::toLowerCaseWithUnderscores).map(String::trim).toList();

        if (fileContent != null) {
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                String modID = modIDF.get(i % modIDF.size());
                String updatedContent = fileContent.replace("{texture_name}", name)
                        .replace("{mod_id}", modID);

                File outputFile = new File(outputPathLable.getText(), name + ".json");

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                    writer.write(updatedContent);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to write file: " + outputFile.getAbsolutePath());
                }
            }
        }

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Successfully Generated Entity Class.");

        // When OK is pressed, reset the fields
        successAlert.setOnHidden(evt -> {
            outputPathLable.setText("Select output folder");
            nameField.clear();
            modIdField.clear();
            entityTypeComboBox.setValue(null);
        });

        successAlert.showAndWait();
    }

    private void generateGrassBlockModel() {
        String fileContent = loadResourceFile("blocks/GrassBlockCode.txt");
        List<String> names = Arrays.stream(nameField.getText().split(",")).map(AppUtils::toLowerCaseWithUnderscores).map(String::trim).toList();
        List<String> modIDF = Arrays.stream(modIdField.getText().split(",")).map(AppUtils::toLowerCaseWithUnderscores).map(String::trim).toList();

        if (fileContent != null) {
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                String modID = modIDF.get(i % modIDF.size());
                String updatedContent = fileContent.replace("{texture_name}", name)
                        .replace("{mod_id}", modID);

                File outputFile = new File(outputPathLable.getText(), name + ".json");

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                    writer.write(updatedContent);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to write file: " + outputFile.getAbsolutePath());
                }
            }
        }

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Successfully Generated Entity Class.");

        // When OK is pressed, reset the fields
        successAlert.setOnHidden(evt -> {
            outputPathLable.setText("Select output folder");
            nameField.clear();
            modIdField.clear();
            entityTypeComboBox.setValue(null);
        });

        successAlert.showAndWait();
    }

    private void generateFenceGateBlockModels() {
        // Load file templates
        String fenceGateContent = loadResourceFile("blocks/FenceGateBlockCode.txt");
        String fenceGateOpenContent = loadResourceFile("blocks/FenceGateOpenBlockCode.txt");
        String fenceGateWallContent = loadResourceFile("blocks/FenceGateWallBlockCode.txt");

        // Retrieve names and mod IDs from input fields
        List<String> names = Arrays.stream(nameField.getText().split(","))
                .map(AppUtils::toLowerCaseWithUnderscores)
                .map(String::trim)
                .toList();
        List<String> modIDs = Arrays.stream(modIdField.getText().split(","))
                .map(AppUtils::toLowerCaseWithUnderscores)
                .map(String::trim)
                .toList();

        if (fenceGateContent != null && fenceGateOpenContent != null && fenceGateWallContent != null) {
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                String modID = modIDs.get(i % modIDs.size());

                // Create the vertical log block JSON
                String verticalFileContent = fenceGateContent.replace("{texture_name}", name)
                        .replace("{mod_id}", modID);
                File verticalOutputFile = new File(outputPathLable.getText(), name + ".json");
                writeToFile(verticalOutputFile, verticalFileContent);

                // Create the horizontal log block JSON
                String horizontalFileContent = fenceGateOpenContent.replace("{texture_name}", name)
                        .replace("{mod_id}", modID);
                File horizontalOutputFile = new File(outputPathLable.getText(), name + "_open.json");
                writeToFile(horizontalOutputFile, horizontalFileContent);

                // Create the side log block JSON
                String sideFileContent = fenceGateWallContent.replace("{texture_name}", name)
                        .replace("{mod_id}", modID);
                File sideOutputFile = new File(outputPathLable.getText(), name + "_wall.json");
                writeToFile(sideOutputFile, sideFileContent);
            }
        }

        // Show success alert
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Successfully Generated Fence Gate Block Files.");

        // Reset fields when OK is pressed
        successAlert.setOnHidden(evt -> {
            outputPathLable.setText("Select output folder");
            nameField.clear();
            modIdField.clear();
            entityTypeComboBox.setValue(null);
        });

        successAlert.showAndWait();
    }

    private void generateFenceBlockModels() {
        String fenceContent = loadResourceFile("blocks/FenceInvBlockCode.txt");
        String fencePostContent = loadResourceFile("blocks/FencePostBlockCode.txt");
        String fenceSideContent = loadResourceFile("blocks/FenceSideBlockCode.txt");

        // Retrieve names and mod IDs from input fields
        List<String> names = Arrays.stream(nameField.getText().split(","))
                .map(AppUtils::toLowerCaseWithUnderscores)
                .map(String::trim)
                .toList();
        List<String> modIDs = Arrays.stream(modIdField.getText().split(","))
                .map(AppUtils::toLowerCaseWithUnderscores)
                .map(String::trim)
                .toList();

        if (fenceContent != null && fencePostContent != null && fenceSideContent != null) {
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                String modID = modIDs.get(i % modIDs.size());

                // Create the vertical log block JSON
                String verticalFileContent = fenceContent.replace("{texture_name}", name)
                        .replace("{mod_id}", modID);
                File verticalOutputFile = new File(outputPathLable.getText(), name + ".json");
                writeToFile(verticalOutputFile, verticalFileContent);

                // Create the horizontal log block JSON
                String horizontalFileContent = fencePostContent.replace("{texture_name}", name)
                        .replace("{mod_id}", modID);
                File horizontalOutputFile = new File(outputPathLable.getText(), name + "_open.json");
                writeToFile(horizontalOutputFile, horizontalFileContent);

                // Create the side log block JSON
                String sideFileContent = fenceSideContent.replace("{texture_name}", name)
                        .replace("{mod_id}", modID);
                File sideOutputFile = new File(outputPathLable.getText(), name + "_wall.json");
                writeToFile(sideOutputFile, sideFileContent);
            }
        }

        // Show success alert
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Successfully Generated Fence Gate Block Files.");

        // Reset fields when OK is pressed
        successAlert.setOnHidden(evt -> {
            outputPathLable.setText("Select output folder");
            nameField.clear();
            modIdField.clear();
            entityTypeComboBox.setValue(null);
        });

        successAlert.showAndWait();
    }

    private void generateSlabBlockModels() {
        String slabContent = loadResourceFile("blocks/SlabBlockCode.txt");
        String slabTopContent = loadResourceFile("blocks/SlabTopBlockCode.txt");

        List<String> names = Arrays.stream(nameField.getText().split(","))
                .map(AppUtils::toLowerCaseWithUnderscores)
                .map(String::trim)
                .toList();
        List<String> modIDs = Arrays.stream(modIdField.getText().split(","))
                .map(AppUtils::toLowerCaseWithUnderscores)
                .map(String::trim)
                .toList();

        if (slabContent != null && slabTopContent != null) {
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                String modID = modIDs.get(i % modIDs.size());

                String verticalFileContent = slabContent.replace("{texture_name}", name)
                        .replace("{mod_id}", modID);
                File verticalOutputFile = new File(outputPathLable.getText(), name + ".json");
                writeToFile(verticalOutputFile, verticalFileContent);

                String horizontalFileContent = slabTopContent.replace("{texture_name}", name)
                        .replace("{mod_id}", modID);
                File horizontalOutputFile = new File(outputPathLable.getText(), name + "_open.json");
                writeToFile(horizontalOutputFile, horizontalFileContent);
            }
        }

        // Show success alert
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Successfully Generated Fence Gate Block Files.");

        // Reset fields when OK is pressed
        successAlert.setOnHidden(evt -> {
            outputPathLable.setText("Select output folder");
            nameField.clear();
            modIdField.clear();
            entityTypeComboBox.setValue(null);
        });

        successAlert.showAndWait();
    }

    private void generateLogBlockModels() {
        String logBlockContent = loadResourceFile("blocks/LogColumnBlockCode.txt");
        String logBlockHorizContent = loadResourceFile("blocks/LogColumnHorizBlockCode.txt");

        List<String> names = Arrays.stream(nameField.getText().split(","))
                .map(AppUtils::toLowerCaseWithUnderscores)
                .map(String::trim)
                .toList();
        List<String> modIDs = Arrays.stream(modIdField.getText().split(","))
                .map(AppUtils::toLowerCaseWithUnderscores)
                .map(String::trim)
                .toList();

        if (logBlockContent != null && logBlockHorizContent != null) {
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                String modID = modIDs.get(i % modIDs.size());

                String verticalFileContent = logBlockContent.replace("{texture_name}", name)
                        .replace("{mod_id}", modID);
                File verticalOutputFile = new File(outputPathLable.getText(), name + ".json");
                writeToFile(verticalOutputFile, verticalFileContent);

                String horizontalFileContent = logBlockHorizContent.replace("{texture_name}", name)
                        .replace("{mod_id}", modID);
                File horizontalOutputFile = new File(outputPathLable.getText(), name + "_open.json");
                writeToFile(horizontalOutputFile, horizontalFileContent);
            }
        }

        // Show success alert
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Successfully Generated Log Block Files.");

        // Reset fields when OK is pressed
        successAlert.setOnHidden(evt -> {
            outputPathLable.setText("Select output folder");
            nameField.clear();
            modIdField.clear();
            entityTypeComboBox.setValue(null);
        });

        successAlert.showAndWait();
    }

    private void generateStairBlockModels() {
        // Load file templates
        String stairsContent = loadResourceFile("blocks/StairsBlockCode.txt");
        String stairsInnerContent = loadResourceFile("blocks/StairsInnerBlockCode.txt");
        String stairsOuterContent = loadResourceFile("blocks/StairsOuterBlockCode.txt");

        // Retrieve names and mod IDs from input fields
        List<String> names = Arrays.stream(nameField.getText().split(","))
                .map(AppUtils::toLowerCaseWithUnderscores)
                .map(String::trim)
                .toList();
        List<String> modIDs = Arrays.stream(modIdField.getText().split(","))
                .map(AppUtils::toLowerCaseWithUnderscores)
                .map(String::trim)
                .toList();

        if (stairsContent != null && stairsInnerContent != null && stairsOuterContent != null) {
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                String modID = modIDs.get(i % modIDs.size());

                // Create the vertical log block JSON
                String verticalFileContent = stairsContent.replace("{texture_name}", name)
                        .replace("{mod_id}", modID);
                File verticalOutputFile = new File(outputPathLable.getText(), name + ".json");
                writeToFile(verticalOutputFile, verticalFileContent);

                // Create the horizontal log block JSON
                String horizontalFileContent = stairsInnerContent.replace("{texture_name}", name)
                        .replace("{mod_id}", modID);
                File horizontalOutputFile = new File(outputPathLable.getText(), name + "_open.json");
                writeToFile(horizontalOutputFile, horizontalFileContent);

                // Create the side log block JSON
                String sideFileContent = stairsOuterContent.replace("{texture_name}", name)
                        .replace("{mod_id}", modID);
                File sideOutputFile = new File(outputPathLable.getText(), name + "_wall.json");
                writeToFile(sideOutputFile, sideFileContent);
            }
        }

        // Show success alert
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Successfully Generated Fence Gate Block Files.");

        // Reset fields when OK is pressed
        successAlert.setOnHidden(evt -> {
            outputPathLable.setText("Select output folder");
            nameField.clear();
            modIdField.clear();
            entityTypeComboBox.setValue(null);
        });

        successAlert.showAndWait();
    }
}
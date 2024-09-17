package com.bdc.dannyboymodding.windows;

import com.bdc.dannyboymodding.UI.buttons.BrowseButton;
import com.bdc.dannyboymodding.UI.buttons.OutputPathBox;
import com.bdc.dannyboymodding.UI.buttons.SelectFilesButton;
import com.bdc.dannyboymodding.utils.FileTypes;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.bdc.dannyboymodding.utils.AppUtils.*;

public class CreateEntityReg extends VBox {
    public CreateEntityReg() {
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);

        Text title = new Text("Create Bluedude Dragons Dragon Registry:");
        title.setFill(Color.web("#1e90ff"));
        title.setStyle("-fx-font-size: 24px;");

        SelectFilesButton selectFilesButton = new SelectFilesButton(FileTypes.JAVA, "Select Files");

        Text outputPathLabelText = new Text("Output Path:");
        outputPathLabelText.setFill(Color.WHITE);
        outputPathLabelText.setStyle("-fx-font-size: 14px;");

        outputPathLable.setText("Select output folder");
        outputPathLable.setStyle("-fx-background-color: #2e2e2e; -fx-text-fill: white;");
        outputPathLable.setPrefWidth(225);
        outputPathLable.setPrefHeight(20);
        outputPathLable.setAlignment(Pos.CENTER);

        BrowseButton browseButton = new BrowseButton("Browse...");
        OutputPathBox outputPathBox = new OutputPathBox(-10, -43, 0, 0);

        outputPathBox.getChildren().addAll(outputPathLabelText, outputPathLable, browseButton);

        Button generateButton = new Button("Generate EntityReg.txt");
        generateButton.setOnAction(e -> generateEntityRegFile());
        generateButton.setPrefWidth(300);
        generateButton.setStyle("-fx-background-color: #3788ca; -fx-text-fill: white;");

        this.getChildren().addAll(title, selectFilesButton, outputPathBox, generateButton);
    }

    private void generateEntityRegFile() {
        String outputPath = outputPathLable.getText();
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
                writer.newLine();
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to write file: " + outputFile.getAbsolutePath());
        }
    }
}
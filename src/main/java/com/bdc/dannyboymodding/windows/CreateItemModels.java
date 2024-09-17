package com.bdc.dannyboymodding.windows;

import com.bdc.dannyboymodding.UI.buttons.BrowseButton;
import com.bdc.dannyboymodding.UI.buttons.OutputPathBox;
import com.bdc.dannyboymodding.utils.AppUtils;
import com.bdc.dannyboymodding.utils.NewInsets;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import static com.bdc.dannyboymodding.utils.AppUtils.showAlert;

public class CreateItemModels extends VBox {
    private final TextField nameField;
    private final TextField modIdField;
    public CreateItemModels() {
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);

        Text title = new Text("Create Item Models:");
        title.setFill(Color.web("#1e90ff"));
        title.setStyle("-fx-font-size: 24px;");

        VBox topSection = new VBox();
        topSection.setSpacing(90);
        topSection.setAlignment(Pos.TOP_CENTER);
        topSection.getChildren().addAll(title);
        VBox.setMargin(topSection, new Insets(-50, 0, 0, 0));

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
        generateButton.setOnAction(e -> generateItemModel());
        generateButton.setPrefWidth(300);
        generateButton.setStyle("-fx-background-color: #3788ca; -fx-text-fill: white;");

        BrowseButton browseButton = new BrowseButton("Browse...");
        OutputPathBox outputPathBox = new OutputPathBox(-10, -43, 0, 0, 4);
        outputPathBox.getChildren().addAll(outputPathLabelText, outputPathLable, browseButton);

        VBox.setMargin(namesBox, new NewInsets(10, 0, 0, -130));
        VBox.setMargin(modIdBox, new NewInsets(10, 0, 0, -209));
        VBox.setMargin(outputPathBox, new NewInsets(10, 0, 0, -45));
        VBox.setMargin(generateButton, new NewInsets(10, 0, 0, 0));

        this.getChildren().addAll(topSection, namesBox, modIdBox, outputPathBox, generateButton);
    }

    private void generateItemModel() {
        String fileContent = loadResourceFile("items/ItemModel.txt");
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

        // Show success alert with OK button
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Successfully Generated Entity Class.");

        // When OK is pressed, reset the fields
        successAlert.setOnHidden(evt -> {
            outputPathLable.setText("Select output folder");
            nameField.clear();
            modIdField.clear();
        });

        successAlert.showAndWait();
    }
}
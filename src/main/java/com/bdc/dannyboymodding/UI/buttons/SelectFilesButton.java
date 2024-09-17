package com.bdc.dannyboymodding.UI.buttons;

import com.bdc.dannyboymodding.utils.FileTypes;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bdc.dannyboymodding.utils.AppUtils.selectedClassNames;

public class SelectFilesButton extends Button {
    private final FileTypes fileType;

    public SelectFilesButton(FileTypes pFileType, String pText) {
        super(pText);
        this.fileType = pFileType;
        setStyle("-fx-background-color: #3788ca; -fx-text-fill: white;");
        setPrefWidth(300);
        setOnAction(e -> openFileChooser());
    }

    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select " + fileType.getDescription());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileType.getDescription(), fileType.getExtension()));

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if (selectedFiles != null) {
            showCheckboxDialog(selectedFiles);
        }
    }

    private void showCheckboxDialog(List<File> pFiles) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Select Entries");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        List<CheckBox> checkBoxes = new ArrayList<>();
        for (File file : pFiles) {
            String fileName = file.getName();
            String className = fileName.replace("." + getFileExtension(file), "");
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

    private String getFileExtension(File pFile) {
        String name = pFile.getName();
        return name.substring(name.lastIndexOf(".") + 1);
    }
}
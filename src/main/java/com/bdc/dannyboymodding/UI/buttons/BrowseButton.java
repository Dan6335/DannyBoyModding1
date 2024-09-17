package com.bdc.dannyboymodding.UI.buttons;

import javafx.scene.control.Button;

import static com.bdc.dannyboymodding.utils.AppUtils.openDirectoryChooser;

public class BrowseButton extends Button {
    public BrowseButton(String text) {
        super(text);
        setStyle("-fx-background-color: #3788ca; -fx-text-fill: white;");
        setOnAction(e -> openDirectoryChooser());
    }
}
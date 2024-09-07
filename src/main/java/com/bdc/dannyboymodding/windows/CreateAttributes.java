package com.bdc.dannyboymodding.windows;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class CreateAttributes {

    public VBox getView() {
        VBox content = new VBox();
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #1c1c1c; -fx-alignment: center;");

        // Add a title text
        Text title = new Text("Create Attributes:");
        title.setFill(Color.web("#1e90ff"));
        title.setStyle("-fx-font-size: 24px;");

        // Add a description or any other text content
        Text description = new Text("This section allows you to create entity attributes.");
        description.setFill(Color.WHITE);
        description.setStyle("-fx-font-size: 16px;");

        // Add the title and description to the content
        content.getChildren().addAll(title, description);

        return content;
    }
}

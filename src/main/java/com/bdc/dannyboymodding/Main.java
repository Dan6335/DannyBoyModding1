package com.bdc.dannyboymodding;

import com.bdc.dannyboymodding.UI.Sidebar;
import com.bdc.dannyboymodding.utils.AppUtils;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        AppUtils.primaryStage = primaryStage;
        BorderPane root = new BorderPane();
        root.setLeft(new Sidebar());
        StackPane dropdownContainer = new StackPane();
        dropdownContainer.setAlignment(Pos.TOP_LEFT);
        dropdownContainer.setPadding(new Insets(20));
        dropdownContainer.setStyle("-fx-background-color: #1c1c1c;");

        VBox content = new VBox();
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #1c1c1c; -fx-alignment: center;");

        Text title = new Text("Welcome to Dan's Modding Tool!");
        title.setFill(Color.web("#1e90ff"));
        title.setStyle("-fx-font-size: 24px;");

        Text description = new Text("With this tool, you will be able to automate your modding experience.\nPlease select one of the options to the left to begin.");
        description.setFill(Color.WHITE);
        description.setStyle("-fx-font-size: 16px;");

        content.getChildren().addAll(title, description);
        dropdownContainer.getChildren().add(content);
        root.setCenter(dropdownContainer);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Dan's Modding Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
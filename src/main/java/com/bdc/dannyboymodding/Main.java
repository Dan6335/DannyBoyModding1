package com.bdc.dannyboymodding;

import com.bdc.dannyboymodding.utils.WindowUtils;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.*;

public class Main extends Application {
    public static Stage primaryStage;
    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(180); // Further reduced width for smaller buttons
        sidebar.setBackground(new Background(new BackgroundFill(Color.web("#2e2e2e"), CornerRadii.EMPTY, Insets.EMPTY)));
        sidebar.setPadding(new Insets(10, 0, 0, 0));
        sidebar.setSpacing(5); // Added spacing between buttons

        Button createEntityButton = new Button("Create Entity");
        Button createItemButton = new Button("Create Item");
        Button createBlockButton = new Button("Create Block");

        styleButton(createEntityButton);
        styleButton(createItemButton);
        styleButton(createBlockButton);

        createEntityButton.setOnAction(e -> toggleDropdown("entityDropdown"));
        createItemButton.setOnAction(e -> toggleDropdown("itemDropdown"));
        createBlockButton.setOnAction(e -> toggleDropdown("blockDropdown"));

        sidebar.getChildren().addAll(createEntityButton, createItemButton, createBlockButton);

        return sidebar;
    }

    private void styleButton(Button button) {
        button.setPrefWidth(160); // Further reduced width for smaller buttons
        button.setPrefHeight(35); // Further reduced height for smaller buttons
        button.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY)));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-font-size: 14px; -fx-cursor: hand; -fx-alignment: center-left;"); // Further reduced font size
        button.setOnMouseEntered(e -> button.setBackground(new Background(new BackgroundFill(Color.web("#4e4e4e"), CornerRadii.EMPTY, Insets.EMPTY))));
        button.setOnMouseExited(e -> button.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY))));
    }

    private VBox createEntityDropdown() {
        VBox dropdown = new VBox();
        dropdown.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY)));
        dropdown.setBorder(new Border(new BorderStroke(Color.web("#4e4e4e"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        dropdown.setPadding(new Insets(5)); // Reduced padding for smaller dropdown
        dropdown.setSpacing(5); // Reduced spacing for smaller dropdown

        Button createFromNames = new Button("Create From Names");
        Button createAttributes = new Button("Create Bdd Entity");

        styleDropdownButton(createFromNames);
        styleDropdownButton(createAttributes);

        // Set event handler for "Create From Names" to open the new class
        createFromNames.setOnAction(e -> WindowUtils.showCreateFromNames());
        createAttributes.setOnAction(e -> WindowUtils.showCreateBddEntityReg());

        dropdown.getChildren().addAll(createFromNames, createAttributes);
        dropdown.setMaxWidth(160); // Set max width to fit around the buttons
        createFromNames.setMaxWidth(160); // Set max width to fit around the buttons
        createAttributes.setMaxWidth(160);
        dropdown.setMaxHeight(60); // Set max height to fit around the buttons
        return dropdown;
    }

    private VBox createItemDropdown() {
        VBox dropdown = new VBox();
        dropdown.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY)));
        dropdown.setBorder(new Border(new BorderStroke(Color.web("#4e4e4e"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        dropdown.setPadding(new Insets(5)); // Reduced padding for smaller dropdown
        dropdown.setSpacing(5); // Reduced spacing for smaller dropdown

        Button createItemModels = new Button("Create Item Models");
        Button createItemReg = new Button("Create Item Registries");

        styleDropdownButton(createItemModels);
        styleDropdownButton(createItemReg);

        dropdown.getChildren().addAll(createItemModels, createItemReg);
        dropdown.setMaxWidth(160); // Set max width to fit around the buttons
        dropdown.setMaxHeight(60); // Set max height to fit around the buttons
        createItemModels.setMaxWidth(160);
        createItemReg.setMaxWidth(160);
        return dropdown;
    }

    private VBox createBlockDropdown() {
        VBox dropdown = new VBox();
        dropdown.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY)));
        dropdown.setBorder(new Border(new BorderStroke(Color.web("#4e4e4e"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        dropdown.setPadding(new Insets(5)); // Reduced padding for smaller dropdown
        dropdown.setSpacing(5); // Reduced spacing for smaller dropdown

        Button createBlockModels = new Button("Create Block Models");
        Button createBlockReg = new Button("Create Block Registries");

        styleDropdownButton(createBlockModels);
        styleDropdownButton(createBlockReg);

        dropdown.getChildren().addAll(createBlockModels, createBlockReg);
        dropdown.setMaxWidth(160); // Set max width to fit around the buttons
        dropdown.setMaxHeight(60); // Set max height to fit around the buttons
        createBlockModels.setMaxWidth(160);
        createBlockReg.setMaxWidth(160);
        return dropdown;
    }

    private void styleDropdownButton(Button button) {
        button.setPrefWidth(120); // Further reduced width for smaller dropdown buttons
        button.setPrefHeight(25); // Reduced height for smaller dropdown buttons
        button.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY)));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-font-size: 12px; -fx-cursor: hand; -fx-alignment: center-left;"); // Further reduced font size
        button.setOnMouseEntered(e -> button.setBackground(new Background(new BackgroundFill(Color.web("#4e4e4e"), CornerRadii.EMPTY, Insets.EMPTY))));
        button.setOnMouseExited(e -> button.setBackground(new Background(new BackgroundFill(Color.web("#3e3e3e"), CornerRadii.EMPTY, Insets.EMPTY))));
    }

    private void toggleDropdown(String dropdownId) {
        VBox dropdown = null;
        Insets margin = null;

        switch (dropdownId) {
            case "entityDropdown":
                dropdown = createEntityDropdown();
                margin = new Insets(-10, 0, 0, -15); // Custom position for entity dropdown
                break;
            case "itemDropdown":
                dropdown = createItemDropdown();
                margin = new Insets(30, 0, 0, -15); // Custom position for item dropdown
                break;
            case "blockDropdown":
                dropdown = createBlockDropdown();
                margin = new Insets(60, 0, 0, -15); // Custom position for block dropdown
                break;
        }

        if (dropdown != null) {
            BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
            StackPane dropdownContainer = (StackPane) root.getCenter();

            // Remove previous dropdowns but keep the welcome content
            dropdownContainer.getChildren().removeIf(node -> node instanceof VBox && node != dropdownContainer.getChildren().get(0));

            dropdownContainer.getChildren().add(dropdown);
            StackPane.setAlignment(dropdown, Pos.TOP_LEFT);
            StackPane.setMargin(dropdown, margin); // Use individual margin for each dropdown
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;

        BorderPane root = new BorderPane();
        root.setLeft(createSidebar());

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
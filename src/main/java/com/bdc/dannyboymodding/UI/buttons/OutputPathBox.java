package com.bdc.dannyboymodding.UI.buttons;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class OutputPathBox extends HBox {
    public OutputPathBox(int top, int left, int bottom, int right) {
        setSpacing(4);
        setAlignment(Pos.CENTER);
        setMargin(this, new Insets(top, left, bottom, right));
    }
}
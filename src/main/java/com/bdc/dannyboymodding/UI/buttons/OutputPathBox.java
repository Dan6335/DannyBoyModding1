package com.bdc.dannyboymodding.UI.buttons;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class OutputPathBox extends HBox {
    public OutputPathBox(int pTop, int pLeft, int pBottom, int pRight, int pSpacing) {
        setSpacing(pSpacing);
        setAlignment(Pos.CENTER);
        setMargin(this, new Insets(pTop, pLeft, pBottom, pRight));
    }
}
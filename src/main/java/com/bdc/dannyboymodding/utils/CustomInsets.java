package com.bdc.dannyboymodding.utils;

import javafx.geometry.Insets;

public class CustomInsets extends Insets {
    public CustomInsets(double pTop, double pRight, double pBottom, double pLeft) {
        super(pTop, pRight, pBottom, pLeft);
    }

    public CustomInsets(double pValue) {
        super(pValue);
    }

    public CustomInsets(double pVertical, double pHorizontal) {
        super(pVertical, pHorizontal, pVertical, pHorizontal);
    }
}
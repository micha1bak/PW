package com.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Circle circle;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    public void onColortPicker(ActionEvent actionEvent) {
        circle.setFill(colorPicker.getValue());
    }
}

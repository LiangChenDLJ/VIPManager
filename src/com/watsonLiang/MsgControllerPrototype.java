package com.watsonLiang;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

// every inherited controller must has a msgTextField in its corresponding fxml
public abstract class MsgControllerPrototype {
    @FXML
    TextField msgTextField;

    public void displayMessage(String msg){
        msgTextField.setText(msg);
    }

}

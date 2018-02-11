package com.watsonLiang;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegController {
    @FXML
    Button regButton;

    @FXML
    FlowPane attrListPane;

    TextField[] attrInputs;

    @FXML
    public void initialize(){
        attrInputs = new TextField[DataModel.searchAttrDisplay.length];
        for(int i = 0; i < DataModel.searchAttrDisplay.length; i++){
            FlowPane fp = new FlowPane();
            attrInputs[i] = new TextField();
            fp.getChildren().add(new Text(DataModel.searchAttrDisplay[i]));
            fp.getChildren().add(attrInputs[i]);
            attrListPane.getChildren().add(fp);
        }
    };

    @FXML
    void regButtonHandler(MouseEvent e){
        String[] attrs = new String[DataModel.searchAttrDisplay.length];
        for(int i = 0; i < DataModel.searchAttrDisplay.length; i++){
            attrs[i] = attrInputs[i].getText();
        }
        Main.dbconn.insert(attrs);
        Stage stage = (Stage) regButton.getScene().getWindow();
        stage.close();
    }

}

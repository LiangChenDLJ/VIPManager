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

public class RegController extends MsgControllerPrototype {
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
    void regButtonHandler(){
        String[] attrs = new String[DataModel.searchAttrDisplay.length];
        int i;
        for(i = 0; i < DataModel.searchAttrDisplay.length; i++){
            attrs[i] = attrInputs[i].getText();
        }
        boolean[] formatCheck = DataModel.formatCheck(DataModel.searchAttr, attrs);
        String errormsg = "";
        for(i = 0; i < formatCheck.length; i++){
            if(!formatCheck[i])
                errormsg += DataModel.searchAttrDisplay[i] + " 不能为 \"" + attrs[i] + "\" | ";
        }
        if(errormsg.length() == 0){
            Main.dbconn.insert(attrs);
            Stage stage = (Stage) regButton.getScene().getWindow();
            stage.close();
        }else{
            displayMessage("格式错误：" + errormsg);
        }
    }

}

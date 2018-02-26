package com.watsonLiang;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RegController extends MsgControllerPrototype {
    @FXML
    Button regButton;

    @FXML
    GridPane attrListPane;

    TextField[] attrInputs;

    @FXML
    public void initialize(){
        attrInputs = new TextField[DataModel.searchAttrDisplay.length];
        for(int i = 0; i < DataModel.searchAttrDisplay.length; i++){
            attrInputs[i] = new TextField();
            attrListPane.addRow(i, new Text(DataModel.searchAttrDisplay[i]), attrInputs[i]);
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
            try{
                Main.dbconn.insert(attrs);
            }catch(Exception e){
                e.printStackTrace();
                displayMessage("注册失败，请检查输入。");
            }
            Stage stage = (Stage) regButton.getScene().getWindow();
            stage.close();
        }else{
            displayMessage("格式错误：" + errormsg);
        }
    }
}
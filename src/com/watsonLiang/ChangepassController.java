package com.watsonLiang;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ChangepassController extends MsgControllerPrototype{
    @FXML
    PasswordField oldpassInput;

    @FXML
    PasswordField newpassInput;

    @FXML
    PasswordField newpassConfirmInput;

    @FXML
    Button changeButton;

    @FXML
    public void initialize(){

    }

    @FXML
    void changeButtonHandler(){
        String newpass = newpassInput.getText();
        if(newpass.compareTo(newpassConfirmInput.getText()) != 0){
            displayMessage("新密码前后不一致");
            return;
        }
        DBConnector.LoginState loginState = Main.dbconn.login(Main.username, oldpassInput.getText());
        if(loginState != DBConnector.LoginState.success){
            displayMessage("旧密码认证失败");
            return;
        }
        Main.dbconn.changePassword(Main.username, newpass);
        Stage stage = (Stage) changeButton.getScene().getWindow();
        stage.close();
    }
}

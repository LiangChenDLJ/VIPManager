package com.watsonLiang;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class changepassController {
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
    void changeButtonHandler(MouseEvent e){
        String newpass = newpassInput.getText();
        if(newpass.compareTo(newpassInput.getText()) != 0){
            Main.logMessage("新密码前后不一致");
            return;
        }
        DBConnector.LoginState loginState = Main.dbconn.login(Main.username, oldpassInput.getText());
        if(loginState != DBConnector.LoginState.success){
            Main.logMessage("旧密码认证失败");
            return;
        }
        Main.dbconn.changePassword(Main.username, newpass);
        Stage stage = (Stage) changeButton.getScene().getWindow();
        stage.close();
    }
}

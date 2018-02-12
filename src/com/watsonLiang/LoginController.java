package com.watsonLiang;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.security.MessageDigest;

public class LoginController {
    @FXML
    TextField usernameInput;

    @FXML
    PasswordField passwordInput;

    @FXML
    Button loginButton;

    @FXML
    public void initialize(){

    }

    @FXML
    void loginButtonHandler(){
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        DBConnector.LoginState loginState = Main.dbconn.login(username, password);
        switch(loginState){
            case success:
                try {
                    Main.username = username;
                    Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
                    Main.mainStage = new Stage();
                    Main.mainStage.setTitle("会员卡管理系统");
                    Main.mainStage.setScene(new Scene(root, 1000, 500));
                    Main.mainStage.show();
                    Main.loginStage.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
                case passwordMismatch:
                    Main.logMessage("登录密码有误");
                    break;
                case usernameNotExist:
                    Main.logMessage("用户不存在");
                    break;
        }
    }
}

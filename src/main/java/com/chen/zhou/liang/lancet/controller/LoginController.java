package com.chen.zhou.liang.lancet.controller;

import com.chen.zhou.liang.lancet.storage.Authenticator;
import com.chen.zhou.liang.lancet.utils.DisplayableException;
import com.chen.zhou.liang.lancet.utils.MessageDisplayer;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import com.chen.zhou.liang.lancet.StageManager;

public class LoginController {
    @FXML
    TextField usernameInput;

    @FXML
    PasswordField passwordInput;

    @FXML
    Button loginButton;

    @FXML
    TextField messageTextField;

    private final StageManager stageManager;
    private final MessageDisplayer messageDisplayer;
    private final Authenticator authenticator;

    @Inject
    public LoginController(StageManager stageManager, Authenticator authenticator) {
        this.stageManager = stageManager;
        this.messageDisplayer = new MessageDisplayer();
        this.authenticator = authenticator;
    }

    @FXML
    public void initialize() {
        messageDisplayer.initialize(messageTextField);
    }

    @FXML
    void loginButtonHandler(){
        String username = usernameInput.getText();
        try {
            authenticator.authenticate(username, passwordInput.getText());
        } catch (DisplayableException e) {
            messageDisplayer.displayMessage(e.getMessage());
            return;
        }
        try {
            stageManager.showMainStage(username);
        } catch (IOException e) {
            messageDisplayer.displayMessage("[内部错误]启动主界面失败，原因： " + e.getMessage());
            e.printStackTrace();
        }
    }
}
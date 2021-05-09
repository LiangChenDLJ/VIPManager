package com.chen.zhou.liang.lancet.controller;

import com.chen.zhou.liang.lancet.StageManager;
import com.chen.zhou.liang.lancet.storage.Authenticator;
import com.chen.zhou.liang.lancet.utils.DisplayableException;
import com.chen.zhou.liang.lancet.utils.MessageDisplayer;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.jooq.DSLContext;

public class ChangePasswordController {
    @FXML
    PasswordField oldPasswordInput;

    @FXML
    PasswordField newPasswordInput;

    @FXML
    PasswordField newPasswordConfirmInput;

    @FXML
    Button changeButton;

    @FXML
    TextField messageTextField;

    private final DSLContext dslContext;
    private final StageManager stageManager;
    private final MessageDisplayer messageDisplayer;
    private final Authenticator authenticator;

    @Inject
    public ChangePasswordController(DSLContext dslContext, StageManager stageManager, Authenticator authenticator) {
        this.dslContext = dslContext;
        this.stageManager = stageManager;
        this.messageDisplayer = new MessageDisplayer();
        this.authenticator = authenticator;
    }

    @FXML
    public void initialize(){
        messageDisplayer.initialize(messageTextField);
    }

    @FXML
    void changeButtonHandler(){
        String oldPassword = oldPasswordInput.getText();
        String newPassword = newPasswordInput.getText();
        String newPasswordConfirm = newPasswordConfirmInput.getText();
        if(!newPassword.equals(newPasswordConfirm)){
            messageDisplayer.displayMessage("新密码前后不一致");
            return;
        }
        try {
            authenticator.authenticate(stageManager.getActiveUser(), oldPassword);
            authenticator.updatePassword(stageManager.getActiveUser(), newPassword);
        } catch (DisplayableException e) {
            messageDisplayer.displayException(e);
            return;
        }

        stageManager.closeChangePasswordStage();
    }
}

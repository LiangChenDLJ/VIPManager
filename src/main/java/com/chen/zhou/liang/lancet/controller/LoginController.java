package com.chen.zhou.liang.lancet.controller;

import com.chen.zhou.liang.lancet.storage.Authenticator;
import com.chen.zhou.liang.lancet.storage.orm.tables.records.LoginmsgRecord;
import com.chen.zhou.liang.lancet.utils.DisplayableException;
import com.chen.zhou.liang.lancet.utils.MessageDisplayer;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jooq.DSLContext;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    private byte[] concatenateBytes(byte[] bytes1, byte[] bytes2) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes1.length + bytes2.length);
        byteBuffer.put(bytes1);
        byteBuffer.put(bytes2);
        return byteBuffer.array();
    }

    private byte[] getDigest(String password, String salt) throws DecoderException, NoSuchAlgorithmException {
        byte[] passwordSaltBytes = concatenateBytes(password.getBytes(StandardCharsets.UTF_8), Hex.decodeHex(salt));
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(passwordSaltBytes);
        return md.digest();
    }

    private boolean bytesEqual(byte[] bytes1, byte[] bytes2) {
        if (bytes1.length != bytes2.length) {
            return false;
        }
        for (int i = 0; i < bytes1.length; i++) {
           if (bytes1[i] != bytes2[i]) return false;
        }
        return true;
    }

    private boolean verifyPassword(LoginmsgRecord admin, String inputPassword) throws DecoderException, NoSuchAlgorithmException  {
        byte[] inputPasswordSaltDigest = getDigest(inputPassword, admin.getSalt());
        byte[] storedPasswordSaltDigest = Hex.decodeHex(admin.getPasswordhash());
        return bytesEqual(inputPasswordSaltDigest, storedPasswordSaltDigest);
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
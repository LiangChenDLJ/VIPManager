package com.chen.zhou.liang.lancet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.checkerframework.checker.nullness.qual.Nullable;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;

public class StageManager {
    private final Provider<FXMLLoader> fxmlLoaderProvider;
    @Nullable
    private String activeUser;

    private final Stage primaryStage;

    @Nullable
    private Stage activeStage;

    @Nullable
    private Stage registerStage;

    @Nullable
    private Stage changePasswordStage;

    @Inject
    public StageManager(Provider<FXMLLoader> fxmlLoaderProvider, @Annotations.PrimaryStage Stage primaryStage) {
      this.fxmlLoaderProvider = fxmlLoaderProvider;
      this.primaryStage = primaryStage;
      this.activeStage = null;
      this.registerStage = null;
      this.activeUser = null;
    }

    public void showLoginStage() throws IOException {
        Parent root;
        try (InputStream fxmlInputStream = getClass().getResourceAsStream("/view/login.fxml")) {
            root = fxmlLoaderProvider.get().load(fxmlInputStream);
        }
        primaryStage.setTitle("登录");
        setStageIcon(primaryStage);
        primaryStage.setScene(new Scene(root));
        activateAndSwitchStage(primaryStage);
    }

    public void showMainStage(String user) throws IOException {
        this.activeUser = user;
        Parent root = fxmlLoaderProvider.get().load(getClass().getResourceAsStream("/view/main.fxml"));
        Stage mainStage = new Stage();
        mainStage.setTitle("会员卡管理系统-老桥头药品零售有限公司");
        mainStage.setScene(new Scene(root));
        mainStage.setResizable(true);
        setStageIcon(mainStage);
        activateAndSwitchStage(mainStage);
    }

    public void showRegisterStage() throws IOException {
        if (registerStage == null) {
            Parent root = fxmlLoaderProvider.get().load(getClass().getResourceAsStream("/view/reg.fxml"));
            registerStage = new Stage();
            registerStage.setTitle("注册");
            registerStage.setScene(new Scene(root));
            registerStage.initModality(Modality.APPLICATION_MODAL);
            registerStage.setResizable(false);
            setStageIcon(registerStage);
        }
        registerStage.show();
    }

    public void closeRegisterStage() {
        // TODO: warning when trying to close null.
        if (registerStage != null) {
            registerStage.close();
        }
    }

    public void showChangePasswordStage() throws IOException {
        if (changePasswordStage == null) {
            Parent root = fxmlLoaderProvider.get().load(getClass().getResourceAsStream("/view/change_password.fxml"));
            changePasswordStage = new Stage();
            changePasswordStage.setTitle("修改密码");
            changePasswordStage.setScene(new Scene(root));
            changePasswordStage.initModality(Modality.APPLICATION_MODAL);
            changePasswordStage.setResizable(false);
            setStageIcon(changePasswordStage);
        }
        changePasswordStage.show();
    }

    public void closeChangePasswordStage() {
        // TODO: warning when trying to close null.
        if (changePasswordStage != null) {
            changePasswordStage.close();
        }
    }

    private static void setStageIcon(Stage stage) {
        stage.getIcons().add(new Image(StageManager.class.getResourceAsStream("/icon/logo.jpg")));
    }

    private void activateAndSwitchStage(Stage newStage) {
        newStage.show();
        if (activeStage != null) {
            System.out.println("Stage closed:" + activeStage);
            activeStage.close();
        }
        activeStage = newStage;
    }
    public @Nullable String getActiveUser() {
        return activeUser;
    }
}

package com.watsonLiang;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage regStage;
    public static Stage loginStage;
    public static Stage mainStage;
    public static Stage changepassStage;
    public static DBConnector dbconn;
    public static String username;
    @Override
    public void start(Stage primaryStage) throws Exception{
        loginStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        primaryStage.setTitle("登录");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        dbconn = new DBConnector("src/data/VIPManager.db");
        launch(args);
    }

    public static void displayMessage(String log){
        System.out.println("Log : " + log);
    }
}

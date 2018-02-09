package com.watsonLiang;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static DBConnector dbconn;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("VIPManager");
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        dbconn = new DBConnector("data/VIPManager.db");
        launch(args);
    }
}

package com.chen.zhou.liang.lancet;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class LancetApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Injector injector = Guice.createInjector(new ApplicationModule(primaryStage));
        StageManager stageManager = injector.getInstance(StageManager.class);
        stageManager.showLoginStage();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

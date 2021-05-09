package com.chen.zhou.liang.lancet;

import com.chen.zhou.liang.lancet.storage.StorageModule;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class ApplicationModule extends AbstractModule {
    Stage primaryStage;

    public ApplicationModule(Stage primaryStage) {
       this.primaryStage = primaryStage;
    }

    @Override
    protected void configure() {
        install(new StorageModule());
        bind(StageManager.class).in(Singleton.class);
        bind(Stage.class).annotatedWith(Annotations.PrimaryStage.class).toInstance(primaryStage);
    }

    @Provides
    FXMLLoader providesFXMLLoader(Injector injector) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        //The new part. Give fxmlLoader a callback. Controllers will now be instantiated via the container, not FXMLLoader itself.
        fxmlLoader.setControllerFactory(injector::getInstance);
        return fxmlLoader;
    }
}

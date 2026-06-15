package com.zaklad_fryzjerski;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BarberShopApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BarberShopApplication.class.getResource("barber-shop-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Zakład Fryzjerski");
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }
}
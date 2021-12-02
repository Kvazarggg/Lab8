package com.example.lab8;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;



public class Lab8 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Lab8.class.getResource("MainApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.getIcons().add(new Image("https://cdn3.iconfinder.com/data/icons/pixomania/128/anchor-256.png"));
        stage.setTitle("Lab 8 - Bilyi Max");
        stage.setMinWidth(615);
        stage.setMinHeight(440);
        stage.setMaxWidth(615);
        stage.setMaxHeight(440);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
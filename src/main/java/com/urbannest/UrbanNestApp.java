package com.urbannest;

import com.urbannest.controllers.MainController;
import com.urbannest.repository.DatabaseManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UrbanNestApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize local SQLite Base
        DatabaseManager.initializeDatabase();

        MainController mainController = new MainController();
        Scene scene = new Scene(mainController.getRoot(), 1000, 700);

        // Load CSS
        String cssPath = getClass().getResource("/com/urbannest/css/style.css").toExternalForm();
        scene.getStylesheets().add(cssPath);

        primaryStage.setTitle("UrbanNest - Hotel Management System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

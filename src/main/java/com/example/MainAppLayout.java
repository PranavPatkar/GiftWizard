package com.example;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainAppLayout {

    public void start(Stage stage) {
          double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        BorderPane root = new BorderPane();

        AppNavigator navigator = new AppNavigator(root);
        navigator.showHomePage(); // Show HomePage on start

        Scene scene = new Scene(root,screenWidth,screenHeight);
        stage.setMaximized(true);
        stage.centerOnScreen();
        stage.setFullScreen(true);
        stage.setTitle("Gift Wizard");
        stage.setScene(scene);
        stage.show();
    }
}
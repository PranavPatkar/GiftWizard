package com.example;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GiftWizarrdApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        AppNavigator navigator = new AppNavigator(root);
        navigator.showHomePage();

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("styles.css"); // optional CSS

        primaryStage.setTitle("🎁 Gift Wizard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}


package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;


public class ReminderPage {
    private StackPane pane;

    public ReminderPage(AppNavigator navigator) {
        // Root pane
    pane = new StackPane();
    pane.setPrefSize(1366, 768); // Fullscreen size for 15.6 inch display
        

        // Background image
        try {
            Image bgImage = new Image(getClass().getResource("/Assets/Images/P.jpg").toExternalForm());
            ImageView bgImageView = new ImageView(bgImage);
            bgImageView.setFitWidth(1366);
            bgImageView.setFitHeight(Screen.getPrimary().getBounds().getHeight());
            bgImageView.setPreserveRatio(false);
            pane.getChildren().add(bgImageView);
          
             
        } catch (Exception e) {
            System.out.println(" Reminder background image not found.");
        }

        // Central form layout (VBox)
        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(30));

        // Header
        Label header = new Label("📅 Set a Reminder");
        header.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        header.setTextFill(Color.BLACK);

        // DatePicker
        DatePicker datePicker = new DatePicker();
        datePicker.setPrefWidth(300);
        datePicker.setStyle("""
            -fx-background-color: white;
            -fx-font-size: 16px;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-padding: 10;
        """);

        // Set Reminder Button
        Button setReminderBtn = new Button("🔔 Set Reminder");
        setReminderBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        setReminderBtn.setPrefWidth(200);
        setReminderBtn.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: black;
            -fx-border-color: #000000;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-padding: 10 20;
            -fx-cursor: hand;
        """);

        // Status Label
        Label status = new Label();
        status.setFont(Font.font("Segoe UI", 16));
        status.setTextFill(Color.BLACK);

        // Set button action
        setReminderBtn.setOnAction(e -> {
            if (datePicker.getValue() != null) {
                status.setText("✅ Reminder set for: " + datePicker.getValue());
            } else {
                status.setText("⚠ Please select a date.");
            }
        });

        // Add to content
        content.getChildren().addAll(header, datePicker, setReminderBtn, status);

        // Add content to pane
        pane.getChildren().add(content);
    }

    public StackPane getPane() {
        return pane;
    }
}

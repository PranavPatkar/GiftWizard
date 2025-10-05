package com.example;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;

public class RegisterPage {

    public void start(Stage stage) {
        StackPane root = new StackPane();

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        // Background image
        try {
            Image bgImage = new Image(getClass().getResource("/Assets/Images/I.jpg").toExternalForm());
            ImageView bgImageView = new ImageView(bgImage);
            bgImageView.setFitWidth(screenWidth);
            bgImageView.setFitHeight(screenHeight);
            bgImageView.setPreserveRatio(false);
            root.getChildren().add(bgImageView);
        } catch (Exception e) {
            System.out.println("Background image not found.");
        }

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);

        Label title = new Label("🎁 Register to Gift Wizard");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
        title.setTextFill(Color.WHITE);
        title.setEffect(new DropShadow(5, Color.BLACK));

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        styleField(emailField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleField(passwordField);

        // --- Role selection ComboBox ---
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("User", "Admin");
        roleComboBox.setValue("User"); // Default selection
        roleComboBox.setStyle(
            "-fx-background-color: rgba(255,255,255,0.21);" +
            "-fx-text-fill: #303030;" +
            "-fx-border-color: #ececec;" +
            "-fx-border-radius: 14;" +
            "-fx-background-radius: 14;" +
            "-fx-padding: 10 15 10 15;"
        );
        roleComboBox.setMaxWidth(340);

        Label statusLabel = new Label();
        statusLabel.setTextFill(Color.LIGHTPINK);
        statusLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));

        Button registerBtn = new Button("Register");
        Button backBtn = new Button("← Back to Login");
        styleButton(registerBtn, "#2196F3");
        styleButton(backBtn, "#BDBDBD");

        registerBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String role = roleComboBox.getValue();

            if (email.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please fill in all fields.");
            } else if (!email.contains("@") || password.length() < 6) {
                statusLabel.setText("Invalid email or weak password.");
            } else {
                // Pass role to your registration backend/service!
                boolean success = FirebaseAuthService.register(email, password, role);
                if (success) {
                    showCustomPopup(stage, "✅ Registration Successful", "You have registered successfully as " + role + ". Please log in now.");
                    new LoginPage().start(stage);
                } else {
                    statusLabel.setTextFill(Color.RED);
                    statusLabel.setText("Registration failed.");
                }
            }
        });

        backBtn.setOnAction(e -> new LoginPage().start(stage));

        HBox buttons = new HBox(30, registerBtn, backBtn);
        buttons.setAlignment(Pos.CENTER);

        content.getChildren().addAll(title, emailField, passwordField, roleComboBox, buttons, statusLabel);
        root.getChildren().add(content);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.setTitle("Register - Gift Wizard");
        stage.setMaximized(true);
        stage.show();
    }

    private void styleField(TextField field) {
        field.setMaxWidth(340);
        field.setFont(Font.font("Segoe UI", 16));
        field.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.18);" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: #eeeeee;" +
            "-fx-border-color: white;" +
            "-fx-border-radius: 14;" +
            "-fx-background-radius: 14;" +
            "-fx-padding: 10 16 10 16;"
        );
    }

    private void styleButton(Button button, String bgColor) {
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        button.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 10 30 10 30;" +
            "-fx-cursor: hand;"
        );
        button.setOnMouseEntered(e ->
            button.setStyle(
                "-fx-background-color: derive(" + bgColor + ", 20%);" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 12;" +
                "-fx-padding: 10 30 10 30;" +
                "-fx-cursor: hand;"
            )
        );
        button.setOnMouseExited(e ->
            button.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 12;" +
                "-fx-padding: 10 30 10 30;" +
                "-fx-cursor: hand;"
            )
        );
    }

    // Custom styled popup
    private void showCustomPopup(Stage ownerStage, String titleText, String messageText) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(ownerStage);
        popupStage.initStyle(StageStyle.TRANSPARENT);

        VBox popupContent = new VBox(20);
        popupContent.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 20;");
        popupContent.setPadding(new Insets(30));
        popupContent.setAlignment(Pos.CENTER);
        popupContent.setEffect(new DropShadow(20, Color.BLACK));

        Label title = new Label(titleText);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#2E7D32"));

        Label message = new Label(messageText);
        message.setFont(Font.font("Segoe UI", 16));
        message.setTextFill(Color.DIMGRAY);
        message.setWrapText(true);
        message.setAlignment(Pos.CENTER);

        Button okBtn = new Button("OK");
        styleButton(okBtn, "#43A047");
        okBtn.setOnAction(e -> popupStage.close());

        popupContent.getChildren().addAll(title, message, okBtn);

        StackPane root = new StackPane(popupContent);
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);"); // semi-transparent background

        Scene scene = new Scene(root, 400, 220);
        scene.setFill(Color.TRANSPARENT);
        popupStage.setScene(scene);
        popupStage.centerOnScreen();
        popupStage.showAndWait();
    }
}

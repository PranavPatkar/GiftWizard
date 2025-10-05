package com.example;


import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class LoginPage {
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();


        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();


        try {
            Image bgImage = new Image(getClass().getResource("/Assets/Images/I.jpg").toExternalForm());
            ImageView bgImageView = new ImageView(bgImage);
            bgImageView.setFitWidth(screenWidth);
            bgImageView.setFitHeight(screenHeight);
            bgImageView.setPreserveRatio(false);
            root.getChildren().add(bgImageView);
        } catch (Exception e) {
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #27367a, #e6f0ff);");
        }


        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPrefWidth(400);


        Label title = new Label("🎁 Welcome to Gift Wizard");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
        title.setTextFill(Color.WHITE);


        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        styleInputField(emailField);


        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleInputField(passwordField);


        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("User", "Admin");
        roleComboBox.setValue("User");
        roleComboBox.setMaxWidth(320);
        roleComboBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);"
                        + "-fx-text-fill: white;"
                        + "-fx-border-color: white;"
                        + "-fx-border-radius: 12;"
                        + "-fx-background-radius: 12;"
                        + "-fx-padding: 10 15 10 15;");


        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: #ffcccc; -fx-font-size: 13px;");


        Button loginButton = new Button("Login");
        styleButton(loginButton, "#28a745");
        Button registerButton = new Button("Register");
        styleButton(registerButton, "#007bff");


        HBox buttons = new HBox(30, loginButton, registerButton);
        buttons.setAlignment(Pos.CENTER);


        content.getChildren().addAll(title, emailField, passwordField, roleComboBox, buttons, statusLabel);
        root.getChildren().add(content);


        loginButton.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String roleSelected = roleComboBox.getValue();


            if (email.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please fill in all fields.");
                return;
            }
            // Attempt login and get role according to backend
            String foundRole = FirebaseAuthService.loginAndGetRole(email, password);
            if (foundRole == null) {
                statusLabel.setText("Invalid credentials or user not found.");
            } else if (roleSelected.equals("Admin") && !foundRole.equals("Admin")) {
                statusLabel.setText("Access denied: You are not an admin.");
            } else if (foundRole.equals("Admin")) {
                new GiftAdminPage().start(primaryStage); // admin gets admin page
            } else {
                new MainAppLayout().start(primaryStage); // user gets app
            }
        });


        registerButton.setOnAction(e -> {
            new RegisterPage().start(primaryStage);
        });


        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login - Gift Wizard");
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(true);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    private void styleInputField(TextField field) {
        field.setMaxWidth(320);
        field.setFont(Font.font("Segoe UI", 16));
        field.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);"
                        + "-fx-text-fill: white;"
                        + "-fx-prompt-text-fill: #eeeeee;"
                        + "-fx-border-color: white;"
                        + "-fx-border-radius: 12;"
                        + "-fx-background-radius: 12;"
                        + "-fx-padding: 10 15 10 15;");
    }


    private void styleButton(Button button, String color) {
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        button.setStyle(
                "-fx-background-color: " + color + ";"
                        + "-fx-text-fill: white;"
                        + "-fx-background-radius: 12;"
                        + "-fx-padding: 10 30 10 30;"
                        + "-fx-cursor: hand;");
    }
}
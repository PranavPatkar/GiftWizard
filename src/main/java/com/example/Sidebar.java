package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Sidebar {
    private VBox pane;

    public Sidebar(AppNavigator navigator) {
        pane = new VBox(25);
        pane.setPadding(new Insets(50, 20, 50, 20));
        pane.setPrefWidth(250);
        pane.setAlignment(Pos.TOP_CENTER);

        // Sidebar styling
        pane.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #ffffff, #f8f8f8);
            -fx-background-radius: 25;
            -fx-border-radius: 25;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 12, 0.4, 0, 4);
        """);

        Label title = new Label("🎁 Gift Wizard");
        title.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 24));
        title.setTextFill(Color.web("#2c3e50"));
        title.setStyle("""
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 2, 0.5, 0, 2);
        """);

        // Navigation buttons
        Button homeBtn = createNavButton("🏠  Home");
        Button giftFinderBtn = createNavButton("🎯  Gift Finder");
        Button wishlistBtn = createNavButton("📝  Wishlist");
        Button reminderBtn = createNavButton("⏰  Reminders");
        Button aboutBtn = createNavButton("❓  About");

        // Logout button
        Button logoutBtn = new Button("🚪 Logout");
        logoutBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        logoutBtn.setPrefWidth(200);
        logoutBtn.setPrefHeight(45);
        logoutBtn.setTextFill(Color.WHITE);
        logoutBtn.setStyle(primaryButtonStyle("#e74c3c"));

        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle(primaryButtonHoverStyle("#c0392b")));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle(primaryButtonStyle("#e74c3c")));

        // Rich UI Logout Popup
        logoutBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Logout Confirmation");
            confirm.setHeaderText("⚠️ Are you sure you want to logout?");
            confirm.setContentText("Your current session will end. You will be returned to the login screen.");

            DialogPane dialogPane = confirm.getDialogPane();
            dialogPane.setStyle("""
                -fx-background-color: linear-gradient(to bottom, #ffffff, #f4f4f4);
                -fx-border-radius: 12;
                -fx-background-radius: 12;
                -fx-padding: 20;
                -fx-font-family: 'Segoe UI';
            """);

            dialogPane.lookup(".header-panel").setStyle("""
                -fx-background-color: transparent;
                -fx-padding: 10 0 10 0;
            """);

            ButtonType okButton = ButtonType.OK;
            ButtonType cancelButton = ButtonType.CANCEL;
            confirm.getButtonTypes().setAll(okButton, cancelButton);

            Button ok = (Button) dialogPane.lookupButton(okButton);
            ok.setStyle("""
                -fx-background-color: #e74c3c;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-cursor: hand;
                -fx-padding: 6 14 6 14;
            """);

            Button cancel = (Button) dialogPane.lookupButton(cancelButton);
            cancel.setStyle("""
                -fx-background-color: #bdc3c7;
                -fx-text-fill: black;
                -fx-font-weight: normal;
                -fx-background-radius: 10;
                -fx-cursor: hand;
                -fx-padding: 6 14 6 14;
            """);

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    SessionManager.clearSession();
                    Stage stage = (Stage) pane.getScene().getWindow();
                    new LoginPage().start(stage);
                }
            });
        });

        // Navigation Actions
        homeBtn.setOnAction(e -> navigator.showHomePage());
        giftFinderBtn.setOnAction(e -> navigator.showGiftFinderPage());
        wishlistBtn.setOnAction(e -> navigator.showWishlistPage());
        reminderBtn.setOnAction(e -> navigator.showReminderPage());
        aboutBtn.setOnAction(e -> navigator.showAboutPage());

        // Layout
        pane.getChildren().addAll(
            title,
            homeBtn,
            giftFinderBtn,
            wishlistBtn,
            reminderBtn,
            aboutBtn,
            new Separator(),
            logoutBtn
        );
    }

    private Button createNavButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 15));
        button.setPrefWidth(200);
        button.setPrefHeight(42);
        button.setTextFill(Color.web("#2c3e50"));
        button.setStyle(navButtonStyle("#f4f4f4"));

        button.setOnMouseEntered(e -> button.setStyle(navButtonHoverStyle("#eaeaea")));
        button.setOnMouseExited(e -> button.setStyle(navButtonStyle("#f4f4f4")));

        return button;
    }

    private String navButtonStyle(String bgColor) {
        return String.format("""
            -fx-background-color: %s;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-border-color: #eee6e6ff;
            -fx-border-width: 1;
            -fx-cursor: hand;
        """, bgColor);
    }

    private String navButtonHoverStyle(String bgColor) {
        return String.format("""
            -fx-background-color: %s;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-border-color: #cccccc;
            -fx-cursor: hand;
        """, bgColor);
    }

    private String primaryButtonStyle(String bgColor) {
        return String.format("""
            -fx-background-color: %s;
            -fx-background-radius: 12;
            -fx-cursor: hand;
            -fx-border-color: transparent;
        """, bgColor);
    }

    private String primaryButtonHoverStyle(String hoverColor) {
        return String.format("""
            -fx-background-color: %s;
            -fx-background-radius: 12;
            -fx-cursor: hand;
        """, hoverColor);
    }

    public VBox getPane() {
        return pane;
    }
}

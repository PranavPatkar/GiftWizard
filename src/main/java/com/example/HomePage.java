package com.example;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class HomePage {
    private StackPane pane;

    public HomePage(AppNavigator navigator) {
        // Main container
        pane = new StackPane();

        // Background image
        BackgroundImage backgroundImage = new BackgroundImage(
            new Image(getClass().getResource("/Assets/Images/N.jpg").toExternalForm(), 800, 600, false, true),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        pane.setBackground(new Background(backgroundImage));

        VBox content = new VBox(45); // generous spacing for elegance
        content.setAlignment(Pos.CENTER);

        // Large, attractive welcome label
        Label welcomeLabel = new Label("✨ Welcome to Gift Wizard ✨");
        welcomeLabel.setFont(Font.font("Segoe Script", 48));
        welcomeLabel.setStyle(
            "-fx-text-fill: linear-gradient(to right, #ff8ae2, #90e0ff, #ffd28d);"
            + "-fx-font-weight: bold;"
            + "-fx-effect: dropshadow(gaussian, #3A015C, 14, 0.7, 3, 8);"
            + "-fx-padding: 24 0 36 0;"
        );

        // Uniform button style
        String buttonStyle = ""
            + "-fx-background-color: linear-gradient(to right, #d061ff, #8f00ff);"
            + "-fx-text-fill: white;"
            + "-fx-padding: 13 27 13 27;"
            + "-fx-background-radius: 23;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 18px;"
            + "-fx-cursor: hand;"
            + "-fx-effect: dropshadow(gaussian, #5f267a, 7, 0.33, 0, 2);"
            + "-fx-border-color: rgba(255,255,255,0.35);"
            + "-fx-border-width: 2px;"
            + "-fx-border-radius: 20;"
        ;

        // Buttons
        Button trendingBtn = new Button("🚀 Trending Gifts");
        trendingBtn.setFont(Font.font("Arial", 19));
        trendingBtn.setStyle(buttonStyle);
        trendingBtn.setOnAction(e -> navigator.showTrendingGiftsPage());  // Will now pop up TrendingGiftsPage

        Button startBtn = new Button("🎁 Get Your Gift Idea");
        startBtn.setFont(Font.font("Arial", 21));
        startBtn.setStyle(buttonStyle
                + "-fx-background-color: linear-gradient(to right, #ffd28d, #ff8ae2);"
                + "-fx-text-fill: #3A015C;"
            );
        startBtn.setOnAction(e -> navigator.showGiftFinderPage());

        Button popularBtn = new Button("🔥 Most Popular Gifts");
        popularBtn.setFont(Font.font("Arial", 19));
        popularBtn.setStyle(buttonStyle);
        popularBtn.setOnAction(e -> navigator.showPopularGiftsPage());

        // Put buttons in row: (trending | start | popular)
        HBox buttonsRow = new HBox(28);
        buttonsRow.setAlignment(Pos.CENTER);
        buttonsRow.getChildren().addAll(trendingBtn, startBtn, popularBtn);

        content.getChildren().addAll(welcomeLabel, buttonsRow);
        pane.getChildren().add(content);
    }

    public StackPane getPane() {
        return pane;
    }
}

package com.example;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.control.ScrollPane;

public class AboutPage {
    private StackPane root;

    public AboutPage(AppNavigator navigator) {
        root = new StackPane();
        double screenW = Screen.getPrimary().getBounds().getWidth();
        double screenH = Screen.getPrimary().getBounds().getHeight();
        root.setPrefSize(screenW, screenH);

        // Background
        ImageView bgView = null;
        try {
            Image bg = new Image(getClass().getResource("/Assets/Images/Q.jpg").toExternalForm());
            bgView = new ImageView(bg);
        } catch (Exception e) {
            System.out.println("Background image not found for AboutPage.");
            bgView = new ImageView();
            bgView.setStyle("-fx-background-color: #eef1fb;");
        }
        bgView.setFitWidth(screenW);
        bgView.setFitHeight(screenH);
        bgView.setPreserveRatio(false);

        // Overlay (makes text readable)
        Region overlay = new Region();
        overlay.setStyle("-fx-background-color: rgba(13, 208, 208, 0.73);");
        overlay.setPrefSize(screenW, screenH);

        // ===== Title/Description Section =====
        Label title = new Label("🎁 GiftWizarrd — Smart Gifting Companion");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 38));
        title.setTextFill(Color.DARKSLATEBLUE);
        title.setEffect(new DropShadow(6, Color.LIGHTSTEELBLUE));
        title.setWrapText(true);

        Label description = new Label(
                "GiftWizard is a modern JavaFX-based desktop application designed to help you discover, plan, and remember the perfect gifts for your loved ones.\n\n" +
                "Key Features:\n" +
                "- Personalized Gift Suggestions: Based on recipient, occasion, budget, and preferences.\n" +
                "- Admin-managed Collections: Includes Trending and Popular gift categories curated by admins.\n" +
                "- Wishlist: Allows you to keep track of gifts you love.\n" +
                "- Seamless Integration: Connects with Firebase for secure authentication and data storage.\n" +
                "- User-friendly Interface: Smooth and intuitive UI for easy gift discovery.\n" +
                "- Smart Search: Filters gifts by occasion, recipient, gender, age group, and budget.\n" +
                "- Dynamic Updates: Trending and Popular gift lists update based on admin selections.\n" +
                "- Support and Mentorship: Built with expert guidance for a flexible and robust experience.\n\n" +
                "Whether you're looking for the perfect birthday gift or planning for special occasions like anniversaries, weddings, or festivals, GiftWizard makes gifting thoughtful and effortless.\n\n" +
                "Enjoy a personalized, organized, and memorable gifting experience with GiftWizard!"
        );
        description.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 19));
        description.setTextFill(Color.BLACK);
        description.setWrapText(true);
        description.setLineSpacing(7);

        // ===== Divider =====
        Line divider = new Line(0, 0, 850, 0);
        divider.setStroke(Color.LIGHTGRAY);
        divider.setStrokeWidth(1.2);
        divider.setEffect(new DropShadow(1.5, Color.GRAY));

        // ===== Developers Section =====
        Label developersTitle = new Label("👨‍💻 Developed By");
        developersTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 23));
        developersTitle.setTextFill(Color.PURPLE);
        developersTitle.setEffect(new DropShadow(3, Color.LAVENDER));

        VBox pranavCard = createDeveloperCard(
                "Pranav Patkar",
                "Lead Developer\nUI/UX Design\nBackend Integration",
                Color.DARKMAGENTA);

        VBox developersSection = new VBox(10, developersTitle, pranavCard);
        developersSection.setAlignment(Pos.CENTER);
        developersSection.setPadding(new Insets(20, 50, 28, 50));
        developersSection.setStyle("-fx-background-color: rgba(255,250,255,0.97); -fx-background-radius: 16;");
        developersSection.setMaxWidth(700);

        // ===== Pack all content in a ScrollPane for good viewing =====
        VBox content = new VBox(
                30,
                title,
                description,
                divider,
                developersSection
        );
        content.setPadding(new Insets(50, 48, 60, 48));
        content.setAlignment(Pos.TOP_CENTER);
        content.setMaxWidth(1200);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPadding(Insets.EMPTY);

        root.getChildren().setAll(bgView, overlay, scroll);
    }

    private VBox createDeveloperCard(String name, String role, Color color) {
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        nameLabel.setTextFill(color);
        nameLabel.setEffect(new DropShadow(3, color.deriveColor(1, 1, 1, 0.35)));

        Label roleLabel = new Label(role);
        roleLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        roleLabel.setTextFill(Color.DIMGRAY);
        roleLabel.setWrapText(true);
        roleLabel.setAlignment(Pos.CENTER);
        roleLabel.setLineSpacing(5);

        VBox card = new VBox(9, nameLabel, roleLabel);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(24));
        card.setStyle("-fx-background-color: rgba(255,255,255,0.95); " +
                "-fx-background-radius: 11; " +
                "-fx-border-color: " + color.toString().replace("0x", "#") + "; " +
                "-fx-border-radius: 11; -fx-border-width: 2;");
        card.setEffect(new DropShadow(9, color.deriveColor(1, 1, 1, 0.20)));
        card.setMinWidth(232);

        return card;
    }

    public StackPane getPane() {
        return root;
    }
}

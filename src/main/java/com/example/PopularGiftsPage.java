package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class PopularGiftsPage {
    private static final String PLACEHOLDER_IMAGE = "https://via.placeholder.com/140x140.png?text=Gift";
    private ScrollPane scrollPane;

    // Reference the shared WishlistManager singleton
    public PopularGiftsPage() {
        // Title
        Label title = new Label("✨ Popular Gifts");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#1d2386"));
        title.setPadding(new Insets(15, 0, 20, 0));
        title.setAlignment(Pos.CENTER);

        // Create grid
        TilePane grid = createGridLayout();

        // Load and add gift cards with "Add to Wishlist" buttons
        List<Gift> popularGifts = GiftService.loadPopularGifts();
        for (Gift gift : popularGifts) {
            VBox giftCard = createGiftCard(gift);
            grid.getChildren().add(giftCard);
        }

        // Main layout
        VBox content = new VBox(title, grid);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(30, 20, 50, 20));
        content.setSpacing(10);

        // Scroll pane setup
        scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    private TilePane createGridLayout() {
        TilePane grid = new TilePane();
        grid.setPadding(new Insets(10));
        grid.setHgap(36);
        grid.setVgap(30);
        grid.setPrefColumns(5);
        grid.setAlignment(Pos.TOP_CENTER);
        return grid;
    }

    private VBox createGiftCard(Gift gift) {
        VBox card = new VBox(13);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(200);
        card.setPadding(new Insets(14, 14, 20, 14));
        card.setStyle(
            "-fx-background-color: rgba(255,255,255,0.98);" +
            "-fx-background-radius:18; -fx-border-radius:18;" +
            "-fx-border-color:#e0eafd; -fx-border-width:1.7;" +
            "-fx-effect: dropshadow(gaussian,#bedbfc,7,0.13,0,2);"
        );

        // Product image
        ImageView imgView = createProductImageView(gift);

        // Text fields
        Label name = createLabel(gift.getName(), "#1d3c61", 16, FontWeight.BOLD, 170);
        Label price = createLabel(gift.getPrice(), "#e67b18", 15, FontWeight.BOLD, -1);
        Label reviews = createLabel(gift.getReviews(), "#388e3c", 13, FontWeight.NORMAL, -1);

        // Add to Wishlist button
        Button addToWishlistBtn = new Button("♡ Add to Wishlist");
        addToWishlistBtn.setMaxWidth(Double.MAX_VALUE);
        addToWishlistBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #f48fb1, #f06292);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 14;"
        );

        addToWishlistBtn.setOnAction(e -> {
            boolean added = WishlistManager.addToWishlist(gift);
            if (added) {
                addToWishlistBtn.setText("✓ Added");
                addToWishlistBtn.setDisable(true);
            }
        });

        // Disable button if gift already in wishlist
        if (WishlistManager.getWishlist().contains(gift)) {
            addToWishlistBtn.setText("✓ Added");
            addToWishlistBtn.setDisable(true);
        }

        card.getChildren().addAll(imgView, name, price, reviews, addToWishlistBtn);
        return card;
    }

    private ImageView createProductImageView(Gift gift) {
        Image img;
        try {
            String path = gift.getImageUrl();
            if (path.startsWith("/Assets")) {
                img = new Image(getClass().getResource(path).toExternalForm(), 140, 140, true, true, true);
            } else {
                img = new Image(path, 140, 140, true, true, true);
            }
            if (img.isError()) throw new Exception();
        } catch (Exception ex) {
            img = new Image(PLACEHOLDER_IMAGE, 140, 140, true, true, true);
        }

        ImageView imgView = new ImageView(img);
        imgView.setSmooth(true);
        imgView.setStyle("-fx-background-radius:16; -fx-effect:dropshadow(gaussian,#ddeefd,11,0.10,2,2);");
        return imgView;
    }

    private Label createLabel(String text, String color, int size, FontWeight weight, double maxWidth) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", weight, size));
        label.setTextFill(Color.web(color));
        if (maxWidth > 0) {
            label.setWrapText(true);
            label.setMaxWidth(maxWidth);
            label.setAlignment(Pos.CENTER);
        }
        return label;
    }

    public ScrollPane getPane() {
        return scrollPane;
    }
}

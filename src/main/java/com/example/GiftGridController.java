package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GiftGridController {
    private static final String PLACEHOLDER_IMAGE = "https://via.placeholder.com/140x140.png?text=Gift";
    private final GiftService giftService = new GiftService();

    public ScrollPane start() {
        GridPane grid = createGiftGrid();

        Label title = new Label("🔥 Trending Gifts");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 38));
        Stop[] stops = new Stop[]{
                new Stop(0, Color.web("#fbad50")),
                new Stop(1, Color.web("#ff9100"))
        };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        title.setTextFill(gradient);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        shadow.setColor(Color.web("#c98811", 0.28));
        title.setEffect(shadow);

        VBox mainContainer = new VBox(30, title, grid);
        mainContainer.setPadding(new Insets(30, 20, 50, 20));
        mainContainer.setAlignment(Pos.TOP_CENTER);

        return createScrollPane(mainContainer);
    }

    private ScrollPane createScrollPane(Node content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        return scrollPane;
    }

    private GridPane createGiftGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(35, 30, 35, 30));
        grid.setHgap(32);
        grid.setVgap(32);
        grid.setAlignment(Pos.TOP_CENTER);

        int cols = 5;
        int row = 0, col = 0;

        for (Gift gift : giftService.loadTrendingGifts()) {
            grid.add(createGiftCard(gift), col, row);
            col = (col + 1) % cols;
            if (col == 0) row++;
        }
        return grid;
    }

    private VBox createGiftCard(Gift gift) {
        VBox card = new VBox(11);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(215);
        card.setPadding(new Insets(12, 12, 16, 12));
        card.setStyle(
                "-fx-background-color: rgba(255,255,255,0.98);" +
                        "-fx-background-radius:18; -fx-border-radius:18;" +
                        "-fx-border-color:#dde7fc; -fx-border-width:1.5;" +
                        "-fx-effect: dropshadow(gaussian,#bedbfc,7,0.13,0,2);"
        );

        ImageView imgView = createProductImageView(gift);
        Label name = createProductNameLabel(gift);
        Label price = createPriceLabel(gift);
        Label reviews = createReviewsLabel(gift);

        Button wishlistBtn = new Button("♡ Add to Wishlist");
        wishlistBtn.setMaxWidth(Double.MAX_VALUE);
        wishlistBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #f48fb1, #f06292);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 14;"
        );

        wishlistBtn.setOnAction(e -> {
            boolean added = WishlistManager.addToWishlist(gift);
            if (added) {
                wishlistBtn.setText("✓ Added");
                wishlistBtn.setDisable(true);
            }
        });

        if (WishlistManager.getWishlist().contains(gift)) {
            wishlistBtn.setText("✓ Added");
            wishlistBtn.setDisable(true);
        }

        card.getChildren().addAll(imgView, name, price, reviews, wishlistBtn);
        return card;
    }

    private ImageView createProductImageView(Gift gift) {
        Image img;
        try {
            img = new Image(gift.getImageUrl(), 140, 140, true, true, true);
            if (img.isError() || img.getException() != null) throw new Exception();
        } catch (Exception ignored) {
            img = new Image(PLACEHOLDER_IMAGE, 140, 140, true, true, true);
        }
        ImageView imgView = new ImageView(img);
        imgView.setSmooth(true);
        imgView.setStyle("-fx-background-radius:16; -fx-effect:dropshadow(gaussian,#ddeefd,11,0.10,2,2);");
        return imgView;
    }

    private Label createProductNameLabel(Gift gift) {
        Label name = new Label(gift.getName());
        name.setFont(Font.font("Segoe UI Semibold", FontWeight.BOLD, 16));
        name.setTextFill(Color.web("#1d3c61"));
        name.setWrapText(true);
        name.setAlignment(Pos.CENTER);
        name.setMaxWidth(190);
        return name;
    }

    private Label createPriceLabel(Gift gift) {
        Label price = new Label(gift.getPrice());
        price.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        price.setTextFill(Color.web("#e67b18"));
        return price;
    }

    private Label createReviewsLabel(Gift gift) {
        Label reviews = new Label(gift.getReviews());
        reviews.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        reviews.setTextFill(Color.web("#388e3c"));
        return reviews;
    }
}

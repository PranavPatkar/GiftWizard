package com.example;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class WishlistPage {
    private static final String PLACEHOLDER_IMAGE = "https://via.placeholder.com/140x140.png?text=Gift";


    public WishlistPage(AppNavigator appNavigator) {
       
    }


    public ScrollPane start() {


        Label title = new Label("🎁 Featured Gift");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 38));
        Stop[] stops = new Stop[] {
                new Stop(0, Color.web("#fbad50")),
                new Stop(1, Color.web("#ff9100"))
        };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        title.setTextFill(gradient);
        title.setEffect(new DropShadow(5, Color.rgb(201, 136, 17, 0.28)));
        title.setPadding(new Insets(0, 0, 20, 0));
        title.setAlignment(Pos.CENTER);


        ListView<Gift> wishlistView = new ListView<>(WishlistManager.getWishlist());
        wishlistView.setCellFactory(param -> new GiftCell());
        wishlistView.setMaxHeight(300);
        wishlistView.setPrefWidth(600);


        VBox wishlistBox = new VBox(10, new Label("💖 Your Wishlist"), wishlistView);
        wishlistBox.setPadding(new Insets(15));
        wishlistBox.setStyle("-fx-background-color: #fce4ec; -fx-background-radius: 12;");
        wishlistBox.setAlignment(Pos.CENTER_LEFT);


        VBox main = new VBox(30, title, wishlistBox);
        main.setAlignment(Pos.TOP_CENTER);
        main.setPadding(new Insets(30, 20, 40, 20));


        return wrapInScrollPane(main);
    }


    private ScrollPane wrapInScrollPane(Node node) {
        ScrollPane pane = new ScrollPane(node);
        pane.setFitToWidth(true);
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return pane;
    }


    private GridPane createSingleGiftGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(35));
        grid.setHgap(32);
        grid.setVgap(32);


        // Only one hardcoded gift
        Gift gift = new Gift("Wooden Handmade Diwali Lamp", "₹749", "⭐⭐⭐⭐ (95 reviews)",
                "https://images.unsplash.com/photo-1604004217770-17ecf025a0a0?auto=format&fit=crop&w=600&q=80");


        VBox giftCard = createGiftCard(gift);
        grid.add(giftCard, 0, 0);


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
                        "-fx-effect: dropshadow(gaussian,#bedbfc,7,0.13,0,2);");


        ImageView imgView = createImage(gift.getImageUrl());
        Label name = createLabel(gift.getName(), "#1d3c61", 16, true);
        Label price = createLabel(gift.getPrice(), "#e67b18", 15, false);
        Label reviews = createLabel(gift.getReviews(), "#388e3c", 13, false);


        Button wishlistBtn = new Button("♡ Add to Wishlist");
        wishlistBtn.setMaxWidth(Double.MAX_VALUE);
        wishlistBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #f48fb1, #f06292);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 14;");
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


    private ImageView createImage(String url) {
        Image img;
        try {
            img = new Image(url, 140, 140, true, true);
        } catch (Exception e) {
            img = new Image(PLACEHOLDER_IMAGE, 140, 140, true, true);
        }
        ImageView imageView = new ImageView(img);
        imageView.setSmooth(true);
        imageView.setStyle("-fx-background-radius:16; -fx-effect:dropshadow(gaussian,#ddeefd,11,0.10,2,2);");
        return imageView;
    }


    private Label createLabel(String text, String color, int fontSize, boolean bold) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", bold ? FontWeight.BOLD : FontWeight.NORMAL, fontSize));
        label.setTextFill(Color.web(color));
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        return label;
    }


    // Wishlist list cell with Remove button
    private static class GiftCell extends ListCell<Gift> {
        private final HBox content = new HBox(15);
        private final ImageView imageView = new ImageView();
        private final Label nameLabel = new Label();
        private final Label priceLabel = new Label();
        private final Label reviewsLabel = new Label();
        private final Button removeButton = new Button("Remove");


        public GiftCell() {
            imageView.setFitWidth(60);
            imageView.setFitHeight(60);


            VBox info = new VBox(nameLabel, priceLabel, reviewsLabel);
            info.setSpacing(3);
            nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
            nameLabel.setTextFill(Color.web("#5e35b1"));
            priceLabel.setFont(Font.font(13));
            priceLabel.setTextFill(Color.web("#f57c00"));
            reviewsLabel.setFont(Font.font(12));
            reviewsLabel.setTextFill(Color.web("#388e3c"));


            removeButton.setStyle(
                    "-fx-background-color: #ef5350;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;");
            removeButton.setOnAction(e -> {
                Gift item = getItem();
                if (item != null) {
                    WishlistManager.removeFromWishlist(item);
                }
            });


            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);


            content.getChildren().addAll(imageView, info, spacer, removeButton);
            content.setAlignment(Pos.CENTER_LEFT);
        }


        @Override
        protected void updateItem(Gift gift, boolean empty) {
            super.updateItem(gift, empty);
            if (empty || gift == null) {
                setGraphic(null);
            } else {
                try {
                    imageView.setImage(new Image(gift.getImageUrl(), 60, 60, true, true));
                } catch (Exception e) {
                    imageView.setImage(new Image(PLACEHOLDER_IMAGE, 60, 60, true, true));
                }
                nameLabel.setText(gift.getName());
                priceLabel.setText(gift.getPrice());
                reviewsLabel.setText(gift.getReviews());
                setGraphic(content);
            }
        }
    }


}
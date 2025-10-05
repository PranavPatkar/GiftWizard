package com.example;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;

import java.io.File;
import java.util.List;

public class GiftAdminPage extends Application {

    private final ObservableList<Gift> gifts = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        Label title = new Label("🏱 Admin Panel: Gift Entries");
        title.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 32));
        title.setTextFill(Color.web("#26308c"));
        title.setEffect(new DropShadow(10, Color.web("#eef0fd")));

        Button logoutBtn = new Button("Logout");
        styleButton(logoutBtn, "#d32f2f");
        logoutBtn.setOnAction(e -> showLogoutWarning(primaryStage, () -> {
            primaryStage.close();
            new LoginPage().start(new Stage());
        }));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBar = new HBox(20, title, spacer, logoutBtn);
        topBar.setPadding(new Insets(16, 28, 16, 32));
        topBar.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(18), Insets.EMPTY)));
        topBar.setEffect(new DropShadow(20, Color.web("#ebe6ff", 0.29)));
        topBar.setAlignment(Pos.CENTER_LEFT);

        TextField nameField = createField("Gift Name");
        TextField priceField = createField("e.g., ₹999");
        TextField reviewField = createField("e.g., 4.5⭐ (1,245 reviews)");
        TextField imageField = createField("Image URL or file path");

        Button imgBrowseBtn = new Button("Browse...");
        styleButton(imgBrowseBtn, "#ecf1f9", "#26308c");
        imgBrowseBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Gift Image");
            File f = chooser.showOpenDialog(primaryStage);
            if (f != null) imageField.setText(f.toURI().toString());
        });

        Button addButton = new Button("Add Gift");
        styleButton(addButton, "#47b881");
        addButton.setOnAction(e -> {
            if (!nameField.getText().isEmpty() && !priceField.getText().isEmpty()) {
                GiftModel giftModel = new GiftModel(
                    nameField.getText(),
                    priceField.getText(),
                    reviewField.getText(),
                    imageField.getText()
                );
                String generatedId = AddGiftData.addgift(giftModel);
                if (generatedId != null) {
                    Gift newGift = new Gift(
                        generatedId,
                        giftModel.getName(),
                        giftModel.getPrice(),
                        giftModel.getReviews(),
                        giftModel.getImageUrl(),
                        false,
                        false
                    );
                    gifts.add(newGift);
                    nameField.clear();
                    priceField.clear();
                    reviewField.clear();
                    imageField.clear();
                } else {
                    System.out.println("Failed to add gift to Firestore.");
                }
            }
        });

        HBox form = new HBox(16, nameField, priceField, reviewField, imageField, imgBrowseBtn, addButton);
        form.setPadding(new Insets(20, 0, 20, 0));
        form.setAlignment(Pos.CENTER_LEFT);

        VBox formCard = new VBox(form);
        formCard.setPadding(new Insets(12, 20, 12, 20));
        formCard.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(18), Insets.EMPTY)));
        formCard.setEffect(new DropShadow(18, Color.web("#e0e7ff", 0.25)));
        formCard.setMaxWidth(1250);

        TableView<Gift> table = new TableView<>(gifts);
        table.setEditable(true);

        TableColumn<Gift, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Gift, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Gift, String> reviewCol = new TableColumn<>("Reviews");
        reviewCol.setCellValueFactory(new PropertyValueFactory<>("reviews"));

        TableColumn<Gift, String> imgCol = new TableColumn<>("Image");
        imgCol.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        imgCol.setCellFactory(tc -> new TableCell<Gift, String>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(48);
                imageView.setFitWidth(48);
                imageView.setPreserveRatio(true);
            }
            @Override
            protected void updateItem(String img, boolean empty) {
                super.updateItem(img, empty);
                if (empty || img == null || img.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        imageView.setImage(new Image(img, 48, 48, true, true, true));
                        setGraphic(imageView);
                    } catch (Exception ex) {
                        setGraphic(null);
                    }
                }
            }
        });

        TableColumn<Gift, Boolean> trendingCol = new TableColumn<>("Trending");
        trendingCol.setCellValueFactory(cellData -> {
            Gift gift = cellData.getValue();
            BooleanProperty prop = new SimpleBooleanProperty(gift.isTrending());
            prop.addListener((obs, oldVal, newVal) -> {
                gift.setTrending(newVal);
                AddGiftData.updateGiftStatus(gift.getId(), newVal, gift.isPopular());
            });
            return prop;
        });
        trendingCol.setCellFactory(CheckBoxTableCell.forTableColumn(trendingCol));
        trendingCol.setEditable(true);

        TableColumn<Gift, Boolean> popularCol = new TableColumn<>("Popular");
        popularCol.setCellValueFactory(cellData -> {
            Gift gift = cellData.getValue();
            BooleanProperty prop = new SimpleBooleanProperty(gift.isPopular());
            prop.addListener((obs, oldVal, newVal) -> {
                gift.setPopular(newVal);
                AddGiftData.updateGiftStatus(gift.getId(), gift.isTrending(), newVal);
            });
            return prop;
        });
        popularCol.setCellFactory(CheckBoxTableCell.forTableColumn(popularCol));
        popularCol.setEditable(true);

        TableColumn<Gift, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button removeBtn = new Button("Remove");
            {
                styleButton(removeBtn, "#d32f2f");
                removeBtn.setOnAction(e -> {
                    Gift gift = getTableView().getItems().get(getIndex());
                    if (gift.getId() != null && !gift.getId().isEmpty()) {
                        RemoveGiftData.removeGift(gift);
                        gifts.remove(gift);
                    } else {
                        System.out.println("Cannot remove gift without Firestore ID");
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeBtn);
            }
        });

        table.getColumns().addAll(nameCol, priceCol, reviewCol, imgCol, trendingCol, popularCol, actionCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMinHeight(360);

        VBox tableCard = new VBox(table);
        tableCard.setPadding(new Insets(16));
        tableCard.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(18), Insets.EMPTY)));
        tableCard.setEffect(new DropShadow(18, Color.web("#e0e7ff", 0.18)));

        VBox layout = new VBox(30, topBar, formCard, tableCard);
        layout.setPadding(new Insets(40, 32, 48, 32));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #f6f9ff, #efeefd 70%, #fcfbff 100%);");

        Scene scene = new Scene(layout, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gift Admin Page");
        primaryStage.show();

        // Load gifts
        List<GiftModel> giftModels = AddGiftData.getAllGifts();
        for (GiftModel model : giftModels) {
            gifts.add(new Gift(
                model.getId(),
                model.getName(),
                model.getPrice(),
                model.getReviews(),
                model.getImageUrl(),
                model.isTrending(),
                model.isPopular()
            ));
        }
    }

    private TextField createField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setFont(Font.font("Segoe UI", 16));
        field.setPrefWidth(200);
        field.setStyle("-fx-background-color: #fbfdff; -fx-border-color: #c4cdfb; " +
                "-fx-border-radius: 14; -fx-background-radius: 14; -fx-padding: 9 16 9 16;");
        return field;
    }

    private void styleButton(Button btn, String bg) {
        styleButton(btn, bg, "white");
    }

    private void styleButton(Button btn, String bg, String fg) {
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        btn.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + fg + "; " +
                "-fx-background-radius: 14; -fx-padding: 8 28 8 28; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: derive(" + bg + ", 15%); " +
                "-fx-text-fill: " + fg + "; -fx-background-radius: 14; " +
                "-fx-padding: 8 28 8 28; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + bg + "; " +
                "-fx-text-fill: " + fg + "; -fx-background-radius: 14; " +
                "-fx-padding: 8 28 8 28; -fx-cursor: hand;"));
    }

    private void showLogoutWarning(Stage owner, Runnable onConfirmLogout) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);

        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(32, 40, 32, 40));
        box.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(20), Insets.EMPTY)));
        box.setEffect(new DropShadow(22, Color.web("#d32f2f", 0.22)));

        Label icon = new Label("⚠");
        icon.setFont(Font.font(50));
        icon.setStyle("-fx-effect: dropshadow(gaussian, #ffb2b2, 10, 0.5, 0, 0);");

        Label title = new Label("Are you sure you want to logout?");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#d32f2f"));
        title.setWrapText(true);

        Label message = new Label("Logging out will end your admin session.\nYou can sign back in at any time.");
        message.setFont(Font.font("Segoe UI", 15));
        message.setTextFill(Color.web("#26308c"));
        message.setWrapText(true);
        message.setTextAlignment(TextAlignment.CENTER);
        message.setMaxWidth(420);
        message.setAlignment(Pos.CENTER);

        Button yesBtn = new Button("Yes, Logout");
        Button cancelBtn = new Button("Cancel");
        styleButton(yesBtn, "#d32f2f");
        styleButton(cancelBtn, "#BDBDBD");

        yesBtn.setOnAction(ev -> {
            dialog.close();
            onConfirmLogout.run();
        });
        cancelBtn.setOnAction(ev -> dialog.close());

        HBox buttonBox = new HBox(20, yesBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER);

        box.getChildren().addAll(icon, title, message, buttonBox);
        StackPane root = new StackPane(box);
        root.setStyle("-fx-background-color: rgba(0,0,0,0.28);");

        Scene scene = new Scene(root, 480, 280);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

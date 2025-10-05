package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GiftFinderPage {
    private StackPane rootPane;
    private ComboBox<String> occasionBox, relationBox, genderBox, ageBox, budgetBox;
    private Button findGiftBtn;
    private VBox suggestionBox;
    private Label resultLabel;
    private String currentGiftIdea = null;

    // Map for tracking unique suggestions per parameter set
    private Map<String, Set<String>> historyMap = new HashMap<>();

    public GiftFinderPage(AppNavigator navigator) {
        rootPane = new StackPane();

        // FULL BACKGROUND IMAGE
        Image backgroundImage = new Image(
                getClass().getResourceAsStream("/Assets/Images/V.jpeg")
        );
        BackgroundImage bgMain = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );
        rootPane.setBackground(new Background(bgMain));

        BorderPane mainPane = new BorderPane();

        // CONTROLS PANEL (LEFT)
        VBox controlsPane = new VBox(17);
        controlsPane.setAlignment(Pos.TOP_LEFT);
        controlsPane.setPrefWidth(330);
        controlsPane.setPadding(new Insets(38, 28, 42, 38));
        controlsPane.setStyle(
            "-fx-background-color: rgba(245,250,255,0.68);" +
            "-fx-background-radius:16; -fx-border-color:#e1dcfc; -fx-border-radius:16;" +
            "-fx-effect: dropshadow(gaussian, #b7c8e3, 16, 0.10, 1, 0);"
        );

        DropShadow shadow = new DropShadow(11, Color.rgb(98,145,197,0.13));
        Label title = new Label("🎁 GiftWizard");
        title.setFont(Font.font("Segoe UI Semibold", 29));
        title.setTextFill(Color.web("#254161"));
        title.setEffect(shadow);

        Label tipLabel = new Label("Don’t like this idea? Click again for a new suggestion!");
        tipLabel.setFont(Font.font("Segoe UI", 13));
        tipLabel.setTextFill(Color.web("#822c9a"));
        tipLabel.setWrapText(true);
        tipLabel.setMaxWidth(229);

        Font labelFont = Font.font("Segoe UI Semibold", 15);

        controlsPane.getChildren().addAll(
            title,
            createLabel("Occasion:", labelFont, shadow),   occasionBox = createComboBox(
                "Birthday", "Anniversary", "Wedding", "Graduation",
                "Diwali", "Rakshabandhan", "Independace Day","Ganesh Chaturthi","Christmas", "Housewarming",
                "Retirement", "Promotion", "Baby Shower", "Farewell","Valentine Day","Other"),
            createLabel("Recipient:", labelFont, shadow),  relationBox = createComboBox(
                "Parent", "Sister","Friend", "Teacher", "Boss",
                "Neighbor", "Cousin", "Grandparent", "Spouse","Students","Self",
                "Boyfriend", "Girlfriend", "Sibling", "Child","Wife","Husband","GymPerson"),
            createLabel("Gender:", labelFont, shadow),     genderBox = createComboBox("Male", "Female", "Other"),
            createLabel("Age Group:", labelFont, shadow),  ageBox = createComboBox("Child", "Teen", "Adult", "Senior"),
            createLabel("Budget:", labelFont, shadow),     budgetBox = createComboBox(
                "Below ₹500", "₹500–₹1000", "₹1000–₹2000",
                "₹2000–₹5000", "₹5000–₹10000", "₹10000+"),
            tipLabel,
            findGiftBtn = createButton("🔍 Find Gift", "#00b4d8", "#fff", "#0077b6"),
            (resultLabel = styledInfoLabel())
            // — no wishlist button/status here anymore
        );
        occasionBox.setPrefWidth(220);
        relationBox.setPrefWidth(220);
        genderBox.setPrefWidth(220);
        ageBox.setPrefWidth(220);
        budgetBox.setPrefWidth(220);

        mainPane.setLeft(controlsPane);

        // SUGGESTION CARD: RIGHT with COLORED BACKGROUND + GLASS EFFECT
        suggestionBox = new VBox(23);
        suggestionBox.setAlignment(Pos.TOP_LEFT);
        suggestionBox.setPrefWidth(465);
        suggestionBox.setPadding(new Insets(45, 38, 38, 48));
        BackgroundImage bgRight = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );
        suggestionBox.setBackground(new Background(bgRight));
        suggestionBox.setStyle(
            "-fx-background-color: rgba(225,240,255,0.77);" +
            "-fx-border-color: #b8d7fc; -fx-border-width: 2;" +
            "-fx-background-radius: 22; -fx-border-radius: 22;" +
            "-fx-effect: dropshadow(gaussian, #d3e5fa, 18, 0.18, 0, 0);"
        );
        mainPane.setCenter(suggestionBox);
        rootPane.getChildren().add(mainPane);

        // LOGIC: UNIQUE, LARGE GEMINI RESPONSE TEXT
        findGiftBtn.setOnAction(e -> {
            if (anyComboUnselected()) {
                resultLabel.setText("Please select all options before searching!");
                return;
            }
            resultLabel.setText("");

            String paramKey = relationBox.getValue() + "|" + genderBox.getValue() + "|" + ageBox.getValue() + "|" +
                    occasionBox.getValue() + "|" + budgetBox.getValue();

            Set<String> alreadySuggested = historyMap.getOrDefault(paramKey, new HashSet<>());
            String alreadyList = alreadySuggested.isEmpty() ? "" :
                    " Please don't suggest any of these gifts again: " +
                    String.join(", ", alreadySuggested) + ".";
            int maxTries = 6;
            int tries = 0;
            String gift = "", why = "", price = "", features = "", stores = "";

            while (tries++ < maxTries) {
                String prompt = String.format(
                        "Suggest a thoughtful physical product as a gift for my %s (Gender: %s, Age Group: %s) on %s, with a budget of %s."
                        + " Please reply in this friendly format, and write everything in simple, natural, and warm language a regular shopper would use:"
                        + "%s"
                        + "\nGift: [one clear, real product or item name]"
                        + "\nWhy: [short, friendly reason that’s easy to understand and makes the user smile]"
                        + "\nPrice: [approximate price or range in rupees]"
                        + "\nFeatures: [describe with short, soft phrases or bullets, not technical terms]"
                        + "\nAvailable at: [well-known shops or websites in India, e.g., Amazon, Flipkart, Nykaa, Croma...]",  
                        relationBox.getValue(), genderBox.getValue(), ageBox.getValue(),
                        occasionBox.getValue(), budgetBox.getValue(), alreadyList
                );
                String response = GiftSuggestionEngine.askGeminiAI(prompt);

                // Parse Gemini response as before
                gift = ""; why = ""; price = ""; features = ""; stores = "";
                for (String line : response.split("\\n")) {
                    String trimmed = line.trim();
                    if (trimmed.toLowerCase().startsWith("gift:")) gift = extract(line, 5);
                    else if (trimmed.toLowerCase().startsWith("why:")) why = extract(line, 4);
                    else if (trimmed.toLowerCase().startsWith("price:")) price = extract(line, 6);
                    else if (trimmed.toLowerCase().startsWith("features:")) features = extract(line, 9);
                    else if (trimmed.toLowerCase().startsWith("available at:")) stores = extract(line, 12);
                }
                if (!alreadySuggested.contains(gift) && !gift.isEmpty())
                    break;
            }
            // Store this suggestion into the history for the current context
            if (!gift.isEmpty()) {
                alreadySuggested.add(gift);
                historyMap.put(paramKey, alreadySuggested);
            }
            currentGiftIdea = gift;
            suggestionBox.getChildren().clear();

            suggestionBox.getChildren().addAll(
                styledSuggestionLabel("🎁 Gift: " + blankIfEmpty(gift), "#204987", 27, 440),
                styledSuggestionLabel((why.isEmpty() ? "" : "Why: " + why), "#239a5c", 20, 440),
                styledSuggestionLabel((price.isEmpty() ? "" : "Price: " + price), "#6d40ad", 19, 440),
                styledSuggestionLabel((features.isEmpty() ? "" : "Features: " + features), "#166db7", 19, 440),
                styledSuggestionLabel((stores.isEmpty() ? "" : "Available at: " + stores), "#2383a0", 18, 440)
            );
            // Wishlist button and status label are no longer present!
        });
    }

    // ------ UI & Helper Methods ------
    private static String extract(String line, int start) {
        return line.length() > start ? line.substring(start).trim() : "";
    }
    private static String blankIfEmpty(String text) {
        return (text == null || text.isEmpty()) ? "[No gift]" : text;
    }
    private boolean anyComboUnselected() {
        return occasionBox.getValue() == null || relationBox.getValue() == null ||
               genderBox.getValue() == null || ageBox.getValue() == null || budgetBox.getValue() == null;
    }
    private Label createLabel(String text, Font font, DropShadow shadow) {
        Label label = new Label(text);
        label.setFont(font);
        label.setTextFill(Color.web("#23536B"));
        label.setEffect(shadow);
        label.setMaxWidth(210);
        label.setWrapText(true);
        return label;
    }
    private ComboBox<String> createComboBox(String... items) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(items);
        comboBox.setPrefWidth(220);
        comboBox.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 5;" +
            "-fx-opacity: 0.99;" +
            "-fx-background-color: linear-gradient(to right,#ffffff,#eaf4fc);"
        );
        return comboBox;
    }
    private Button createButton(String text, String bgColor, String fgColor, String hoverColor) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI Semibold", 15));
        btn.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-text-fill: " + fgColor + ";" +
            "-fx-padding: 10 26 10 26;" +
            "-fx-background-radius: 19;" +
            "-fx-font-weight: bold;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian,#95aeca,1.25,0,1,2);"
        );
        btn.setOnMouseEntered(ev -> btn.setStyle(
            "-fx-background-color: " + hoverColor + ";" +
            "-fx-text-fill: " + fgColor + ";" +
            "-fx-padding: 10 26 10 26;" +
            "-fx-background-radius: 19;" +
            "-fx-font-weight: bold;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian,#95aeca,1.25,0,1,2);"
        ));
        btn.setOnMouseExited(ev -> btn.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-text-fill: " + fgColor + ";" +
            "-fx-padding: 10 26 10 26;" +
            "-fx-background-radius: 19;" +
            "-fx-font-weight: bold;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian,#95aeca,1.25,0,1,2);"
        ));
        return btn;
    }
    private Label styledInfoLabel() {
        Label l = new Label();
        l.setFont(Font.font("Segoe UI", 14));
        l.setTextFill(Color.web("#e25d5d"));
        l.setMaxWidth(205);
        l.setWrapText(true);
        return l;
    }
    private Label styledSuggestionLabel(String text, String color, int fontSize, int width) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", fontSize));
        l.setTextFill(Color.web(color));
        l.setMaxWidth(width);
        l.setWrapText(true);
        return l;
    }
    public StackPane getPane() {
        return rootPane;
    }
}

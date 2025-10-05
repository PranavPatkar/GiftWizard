package com.example;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.awt.Desktop;
import java.net.URI;

public class GiftSuggestionResultPage {
    private VBox pane;

    public GiftSuggestionResultPage(String giftSuggestion, String imageUrl) {
        pane = new VBox(26);
        pane.setAlignment(Pos.CENTER);

        String gift = "", why = "", link = "";
        for (String line : giftSuggestion.split("\\n")) {
            String trimmed = line.trim();
            if (trimmed.toLowerCase().startsWith("gift:")) {
                gift = trimmed.length() > 5 ? trimmed.substring(5).trim() : "";
            } else if (trimmed.toLowerCase().startsWith("why:")) {
                why = trimmed.length() > 4 ? trimmed.substring(4).trim() : "";
            } else if (trimmed.toLowerCase().startsWith("link:")) {
                link = trimmed.length() > 5 ? trimmed.substring(5).trim() : "";
            }
        }
        if (gift.isEmpty()) gift = "gift";
        imageUrl = "https://source.unsplash.com/400x400/?" + gift.replace(" ", "+");
        System.out.println("IMAGE URL: " + imageUrl); // Debug

        Label giftLabel = new Label("🎁 " + gift);
        giftLabel.setFont(Font.font("Arial", 22));
        giftLabel.setWrapText(true);

        Label whyLabel = new Label(why);
        whyLabel.setFont(Font.font("Arial", 15));
        whyLabel.setWrapText(true);

        ImageView imageView = new ImageView(new Image(imageUrl, 220, 220, true, true, true));
        pane.getChildren().addAll(giftLabel, whyLabel, imageView);

        if (link != null && !link.isEmpty() &&
            (link.startsWith("http://") || link.startsWith("https://"))) {
            final String finalLink = link;
            Hyperlink linkLabel = new Hyperlink(finalLink);
            linkLabel.setFont(Font.font("Arial", 14));
            linkLabel.setWrapText(true);
            linkLabel.setOnAction(e -> {
                try {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI(finalLink));
                    }
                } catch (Exception ex) { ex.printStackTrace(); }
            });
            pane.getChildren().add(linkLabel);
        }
    }

    public VBox getPane() { return pane; }
}

package com.example;


import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.Node;


public class AppNavigator {
    private BorderPane root;
    private Sidebar sidebar;


    public AppNavigator(BorderPane root) {
        this.root = root;


        // Sidebar setup
        this.sidebar = new Sidebar(this);
        root.setLeft(sidebar.getPane());


        // Apply rich UI effects to root
        root.setStyle("""
            -fx-background-color: linear-gradient(to bottom right, #ffffff, #f0f0f0);
            -fx-padding: 0;
        """);


        // Optional: DropShadow effect for center content
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(3.0);
        shadow.setOffsetY(3.0);
        shadow.setColor(Color.gray(0.5));
        root.setEffect(shadow);
    }


    //  Allow dynamic center setting from other pages
    public void setCenter(Node node) {
        root.setCenter(node);
    }


    public void showTrendingGiftsPage() {
        root.setCenter(new GiftGridController().start());
    }


    public void showHomePage() {
        root.setCenter(new HomePage(this).getPane());
    }


    public void showGiftFinderPage() {
        root.setCenter(new GiftFinderPage(this).getPane());
    }


    public void showWishlistPage() {
        root.setCenter(new WishlistPage(this).start());
    }


    public void showReminderPage() {
        root.setCenter(new ReminderPage(this).getPane());
    }


    public void showAboutPage() {
        root.setCenter(new AboutPage(this).getPane());
    }


    public void showPopularGiftsPage() {
        root.setCenter(new PopularGiftsPage().getPane());
    }
}
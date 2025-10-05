package com.example;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public final class WishlistManager {
    private static final ObservableList<Gift> wishlist = FXCollections.observableArrayList();


    private WishlistManager() {}


    public static ObservableList<Gift> getWishlist() {
        return FXCollections.unmodifiableObservableList(wishlist);
    }


    public static boolean addToWishlist(Gift gift) {
        if (!wishlist.contains(gift)) {
            wishlist.add(gift);
            return true;
        }
        return false;
    }


    public static boolean removeFromWishlist(Gift gift) {
        return wishlist.remove(gift);
    }


    public static void clearWishlist() {
        wishlist.clear();
    }
}
package com.example;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;

public class GiftService {
    private static final String DEFAULT_IMAGE_URL = "https://via.placeholder.com/300x300.png?text=No+Image";
    
    // Fetch gifts where isTrending == true for Trending Gifts page
    public static List<Gift> loadTrendingGifts() {
        List<Gift> trendingGifts = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();

        try {
            ApiFuture<QuerySnapshot> future = db.collection("gifts")
                .whereEqualTo("isTrending", true)
                .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            for (QueryDocumentSnapshot doc : documents) {
                String id = doc.getId();
                String name = doc.getString("giftName");
                String price = doc.getString("giftPrice");
                String reviews = doc.getString("giftReviews");
                String imageUrl = doc.getString("giftImage");
                Boolean isTrending = doc.getBoolean("isTrending");
                Boolean isPopular = doc.getBoolean("isPopular");

                if (imageUrl == null || imageUrl.isEmpty()) {
                    imageUrl = DEFAULT_IMAGE_URL;
                }

                trendingGifts.add(new Gift(
                    id,
                    name != null ? name : "No Name",
                    price != null ? price : "N/A",
                    reviews != null ? reviews : "No Reviews",
                    imageUrl,
                    isTrending != null && isTrending,
                    isPopular != null && isPopular
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trendingGifts;
    }
    
    // Fetch gifts where isPopular == true for Popular Gifts page
    public static List<Gift> loadPopularGifts() {
        List<Gift> popularGifts = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();

        try {
            ApiFuture<QuerySnapshot> future = db.collection("gifts")
                .whereEqualTo("isPopular", true)
                .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            for (QueryDocumentSnapshot doc : documents) {
                String id = doc.getId();
                String name = doc.getString("giftName");
                String price = doc.getString("giftPrice");
                String reviews = doc.getString("giftReviews");
                String imageUrl = doc.getString("giftImage");
                Boolean isTrending = doc.getBoolean("isTrending");
                Boolean isPopular = doc.getBoolean("isPopular");

                if (imageUrl == null || imageUrl.isEmpty()) {
                    imageUrl = DEFAULT_IMAGE_URL;
                }

                popularGifts.add(new Gift(
                    id,
                    name != null ? name : "No Name",
                    price != null ? price : "N/A",
                    reviews != null ? reviews : "No Reviews",
                    imageUrl,
                    isTrending != null && isTrending,
                    isPopular != null && isPopular
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return popularGifts;
    }
}

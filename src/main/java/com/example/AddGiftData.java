package com.example;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class AddGiftData {

    public static String addgift(GiftModel model) {
        Firestore db = FirebaseConfig.getDb();
        Map<String, Object> data = new HashMap<>();
        data.put("giftName", model.getName());
        data.put("giftPrice", model.getPrice());
        data.put("giftReviews", model.getReviews());
        data.put("giftImage", model.getImageUrl());
        data.put("isTrending", model.isTrending());
        data.put("isPopular", model.isPopular());

        try {
            DocumentReference docRef = db.collection("gifts").document();
            ApiFuture<WriteResult> future = docRef.set(data);
            WriteResult writeResult = future.get();
            System.out.println("Gift added at: " + writeResult.getUpdateTime() + ", ID: " + docRef.getId());
            return docRef.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<GiftModel> getAllGifts() {
        Firestore db = FirebaseConfig.getDb();
        List<GiftModel> gifts = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> future = db.collection("gifts").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                String id = doc.getId();
                String name = doc.getString("giftName");
                String price = doc.getString("giftPrice");
                String reviews = doc.getString("giftReviews");
                String imageUrl = doc.getString("giftImage");
                Boolean isTrending = doc.getBoolean("isTrending");
                Boolean isPopular = doc.getBoolean("isPopular");
                gifts.add(new GiftModel(
                    id, name, price, reviews, imageUrl,
                    isTrending != null ? isTrending : false,
                    isPopular != null ? isPopular : false
                ));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return gifts;
    }

    public static boolean updateGiftStatus(String giftId, boolean isTrending, boolean isPopular) {
        Firestore db = FirebaseConfig.getDb();
        DocumentReference docRef = db.collection("gifts").document(giftId);
        Map<String, Object> updates = new HashMap<>();
        updates.put("isTrending", isTrending);
        updates.put("isPopular", isPopular);

        try {
            ApiFuture<WriteResult> future = docRef.update(updates);
            WriteResult result = future.get();
            System.out.println("Gift status updated at: " + result.getUpdateTime());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public class GetGiftdata {
    public static List<GiftModel> getAllGifts() {
        Firestore db = FirebaseConfig.getDb();
        List<GiftModel> giftList = new ArrayList<>();

        try {
            ApiFuture<QuerySnapshot> future = db.collection("gifts").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            for (QueryDocumentSnapshot doc : documents) {
                String name = doc.getString("giftName");
                String price = doc.getString("giftPrice"); // Or doc.get("giftPrice").toString() if not String
                String reviews = doc.getString("giftReviews");
                String imageUrl = doc.getString("giftImage");

                GiftModel model = new GiftModel(name, price, reviews, imageUrl);
                giftList.add(model);
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return giftList;
    }

}

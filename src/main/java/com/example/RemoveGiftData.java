package com.example;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

public class RemoveGiftData {
    public static void removeGift(Gift giftToRemove) {
        Firestore db = FirebaseConfig.getDb();
        String docId = giftToRemove.getId();
        if (docId == null || docId.isEmpty()) {
            System.out.println("⚠ Gift does not have a Firestore document ID. Cannot delete.");
            return;
        }
        try {
            ApiFuture<WriteResult> future = db.collection("gifts").document(docId).delete();
            WriteResult result = future.get();
            System.out.println("✅ Deleted gift from Firestore: " + docId + " at time: " + result.getUpdateTime());
        } catch (Exception e) {
            System.err.println("❌ Error deleting gift from Firestore: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

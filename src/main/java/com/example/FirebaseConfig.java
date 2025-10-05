package com.example;





import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class FirebaseConfig {
 static Firestore db;
    
    public static void initializeFirebase() {
        try {
            FileInputStream serviceAccount = new FileInputStream("demo\\src\\main\\resources\\giftwizard-ba1f8-firebase-adminsdk-fbsvc-aa987bae6b.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

            FirebaseApp.initializeApp(options);
            //FirebaseConfig.initializeFirebase();
          db=FirestoreClient.getFirestore();
            System.out.println("************** Firebase Initialized **************");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Firestore getDb(){

        if(db==null){
            initializeFirebase();
        }
        return db;
    }

    

     
}
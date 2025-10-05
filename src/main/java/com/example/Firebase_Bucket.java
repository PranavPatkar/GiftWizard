package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;


public class Firebase_Bucket {
    private static final String BUCKET = "agri-sahayata.firebasestorage.app";

    public static String uploadImage(File file) {
        // replace with your bucket name
        String fileName = file.getName();
        String contentType = guessContentType(file); // helper to get correct content type

        try {
            String urlStr = "https://firebasestorage.googleapis.com/v0/b/" + BUCKET + "/o?uploadType=media&name="
                    + URLEncoder.encode(fileName, "UTF-8");

            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", contentType);
            // conn.setRequestProperty("Authorization", "Bearer " + accessToken); // if
            // needed

            try (OutputStream os = conn.getOutputStream(); FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Upload response code: " + responseCode);

            InputStream responseStream = responseCode == 200 ? conn.getInputStream() : conn.getErrorStream();

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            conn.disconnect();

            // Extract download URL manually
            JSONObject jsonObj = new JSONObject(response.toString());
            String token = jsonObj.getString("downloadTokens");

            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
            String downloadUrl = "https://firebasestorage.googleapis.com/v0/b/" + BUCKET + "/o/"
                    + encodedFileName + "?alt=media&token=" + token;
            return downloadUrl;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null; // return null if failed
    }

    private static String guessContentType(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".png"))
            return "image/png";
        if (name.endsWith(".jpg") || name.endsWith(".jpeg"))
            return "image/jpeg";
        return "application/octet-stream"; // default
    }

}
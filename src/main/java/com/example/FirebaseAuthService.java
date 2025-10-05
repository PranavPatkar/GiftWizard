package com.example;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class FirebaseAuthService {
    private static final String API_KEY = "FIREBASE_API_KEY";
    private static final String REGISTER_URL =
            "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY;
    private static final String LOGIN_URL =
            "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY;
    private static final String SESSION_FILE = "session.txt";


    // === Register a new user with role ===
    public static boolean register(String email, String password, String role) {
        try {
            String payload = "{"
                    + "\"email\":\"" + email + "\","
                    + "\"password\":\"" + password + "\","
                    + "\"returnSecureToken\":true"
                    + "}";


            HttpURLConnection conn = (HttpURLConnection) new URL(REGISTER_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);


            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes("UTF-8"));
            }


            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                String response = readResponse(conn.getInputStream());
                if (response.contains("idToken")) {
                    String localId = extractLocalId(response);
                    // Store user role (e.g., Firestore, Realtime DB) - implement this method
                    boolean stored = storeUserRole(localId, email, role);
                    return stored;
                }
            } else {
                String error = readResponse(conn.getErrorStream());
                System.out.println("Registration failed: " + error);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    // === Login user and get role ===
    public static String loginAndGetRole(String email, String password) {
        try {
            String payload = "{"
                    + "\"email\":\"" + email + "\","
                    + "\"password\":\"" + password + "\","
                    + "\"returnSecureToken\":true"
                    + "}";


            HttpURLConnection conn = (HttpURLConnection) new URL(LOGIN_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);


            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes("UTF-8"));
            }


            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                String response = readResponse(conn.getInputStream());
                if (response.contains("idToken")) {
                    saveSession(email);
                    String localId = extractLocalId(response);
                    // Fetch the user's role from database - implement this method
                    String role = fetchUserRole(localId, email);
                    return role; // "Admin" or "User"
                }
            } else {
                String error = readResponse(conn.getErrorStream());
                System.out.println("Login failed: " + error);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // === Store user role (implement actual DB storage logic) ===
    private static boolean storeUserRole(String userId, String email, String role) {
        // TODO: Implement database logic here, e.g., Firestore or Realtime Database write
        System.out.printf("Storing role '%s' for user %s (UID: %s)%n", role, email, userId);
        return true; // Return true if successful
    }


    // === Fetch user role (implement actual DB fetch logic) ===
    private static String fetchUserRole(String userId, String email) {
        // TODO: Implement database logic here, e.g., Firestore or Realtime Database read
        System.out.printf("Fetching role for user %s (UID: %s)%n", email, userId);
        // For testing, if email contains "admin" return "Admin", else "User"
        if (email.toLowerCase().contains("admin")) {
            return "Admin";
        } else {
            return "User";
        }
    }


    // === Extract "localId" (UID) from JSON response (basic parsing) ===
    private static String extractLocalId(String jsonResponse) {
        int index = jsonResponse.indexOf("\"localId\":\"");
        if (index != -1) {
            int start = index + 10;
            int end = jsonResponse.indexOf("\"", start);
            if (end != -1) {
                return jsonResponse.substring(start, end);
            }
        }
        return null;
    }


    // === Save session to file ===
    private static void saveSession(String email) {
        try (FileWriter writer = new FileWriter(SESSION_FILE)) {
            writer.write(email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // === Read input stream to String ===
    private static String readResponse(InputStream is) throws IOException {
        try (Scanner scanner = new Scanner(is)) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }


    // === Optional: Load session from file ===
    public static String loadSession() {
        try {
            return new String(java.nio.file.Files.readAllBytes(java.nio.file.Path.of(SESSION_FILE)));
        } catch (IOException e) {
            return null;
        }
    }


    // === Optional: Clear session ===
    public static void clearSession() {
        try {
            java.nio.file.Files.deleteIfExists(java.nio.file.Path.of(SESSION_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

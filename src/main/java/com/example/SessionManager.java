package com.example;

import java.io.*;
import java.nio.file.*;

public class SessionManager {
    private static final String SESSION_FILE = "session.txt";

    public static void saveSession(String email) {
        try {
            Files.writeString(Path.of(SESSION_FILE), email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadSession() {
        try {
            return Files.readString(Path.of(SESSION_FILE));
        } catch (IOException e) {
            return null;
        }
    }

    public static void clearSession() {
        try {
            Files.deleteIfExists(Path.of(SESSION_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package com.example;

import okhttp3.*;
import org.json.*;

public class GiftSuggestionEngine {
    // Paste your actual API key from Google AI Studio here
    private static final String GEMINI_API_KEY = "AIzaSyBVzC5QoC5Qss4MIb-17ptmZ4B36PmsT_E";

    public static String getSuggestion(String occasion, String relation, String gender, String age, String budget) {
        if (occasion == null || relation == null || gender == null || age == null || budget == null) {
            return "Please select all options.";
        }

        String prompt = String.format(
            "Suggest a unique gift for a %s (%s, %s), for %s. Budget: %s. Be specific and creative.",
            relation, gender, age, occasion, budget
        );

        String aiSuggestion = askGeminiAI(prompt);

        return (aiSuggestion != null && !aiSuggestion.isEmpty())
                ? aiSuggestion
                : "Sorry, I'm unable to suggest a gift at the moment.";
    }

    static String askGeminiAI(String prompt) {
        OkHttpClient client = new OkHttpClient();

        String json = "{ \"contents\": [ { \"parts\": [ { \"text\": \"" + prompt.replace("\"", "\\\"") + "\" } ] } ] }";
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + GEMINI_API_KEY;

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) return "No response from Gemini AI.";
            String responseBody = response.body().string();

            // Print API response for debugging
            System.out.println("Gemini API response:\n" + responseBody);

            JSONObject obj = new JSONObject(responseBody);

            if (obj.has("error")) {
                return "Gemini API error: " + obj.getJSONObject("error").getString("message");
            }
            if (obj.has("candidates")) {
                JSONObject firstCandidate = obj.getJSONArray("candidates").getJSONObject(0);
                if (firstCandidate.has("content")) {
                    JSONObject contentObj = firstCandidate.getJSONObject("content");
                    JSONArray parts = contentObj.getJSONArray("parts");
                    if (parts.length() > 0) {
                        return parts.getJSONObject(0).getString("text").trim();
                    }
                }
            }
            return "Unrecognized Gemini response: " + responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error contacting Gemini AI: " + e.getMessage();
        }
    }
}

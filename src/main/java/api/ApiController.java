package api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ApiController {

    public String getImageUrl(String category) {
        try {
            // ✅ Proper URL encoding to handle spaces and special characters
            String encodedCategory = URLEncoder.encode(category, "UTF-8");

            // ✅ Unsplash API request with encoded query
            String link = "https://api.unsplash.com/photos/random?client_id=3j-6xeJaxReWZoYcrTpib3bEf1P6uN8oh05RE57-i4E&query=" + encodedCategory;

            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();

            // ✅ Manual JSON parsing to extract the "small" image URL
            String json = response.toString();
            String urlsPart = json.split("\"urls\":\\{")[1];
            String smallUrl = urlsPart.split("\"small\":\"")[1].split("\"")[0];

            return smallUrl;

        } catch (Exception e) {
            System.err.println("Error fetching image: " + e.getMessage());
            return null;
        }
    }
}

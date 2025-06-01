package dk.sdu.mmmi.cbse.common.util;

import org.springframework.web.client.RestTemplate;

import static java.lang.System.currentTimeMillis;

public class ScoreClient {
    private static final String BASE_URL = "http://localhost:8081/score";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static String cachedScore = "0";
    private static long lastFetchTime = 0;
    private static final long REFRESH_INTERVAL_MS = 1000;

    public static void addPoints(int points) {
        try {
            restTemplate.postForObject(BASE_URL + "/add?points=" + points, null, Void.class);
        } catch (Exception e) {
            System.err.println("Failed to add score to microservice: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getScore() {
        long now = currentTimeMillis();
        if (now - lastFetchTime > REFRESH_INTERVAL_MS) {
            try {
                String response = restTemplate.getForObject(BASE_URL, String.class);
                cachedScore = (response != null ? response : "0");
                lastFetchTime = now;
            } catch (Exception e) {
                System.err.println("Failed to fetch score from microservice: " + e.getMessage());
            }
        }
        return cachedScore;
    }
}

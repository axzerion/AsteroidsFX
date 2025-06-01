package dk.sdu.mmmi.cbse.common.util;

import org.springframework.web.client.RestTemplate;

public class ScoreClient {
    private static final String BASE_URL = "http://localhost:8081/score";
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void addPoints(int points) {
        try {
            restTemplate.postForObject(BASE_URL + "?point=" + points, null, Long.class);
        } catch (Exception e) {
            System.err.println("Failed to add score to microservice: " + e.getMessage());
        }
    }

    public static int getScore() {
        try {
            Long score = restTemplate.getForObject(BASE_URL, Long.class);
            return score != null ? score.intValue() : -1;
        } catch (Exception e) {
            System.err.println("Failed to fetch score from microservice: " + e.getMessage());
            return -1;
        }
    }
}

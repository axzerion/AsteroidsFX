package dk.sdu.mmmi.cbse.common.util;

import org.springframework.web.client.RestTemplate;

public class ScoreClient {
    private static final String BASE_URL = "http://localhost:8081/score";
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void addPoints(int points) {
        try {
            restTemplate.postForObject(BASE_URL + "/add?points=" + points, null, Void.class);
        } catch (Exception e) {
            System.err.println("Failed to add score to microservice: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void reset() {
        try {
            restTemplate.postForObject(BASE_URL + "/reset", null, Void.class);
        } catch (Exception e) {
            System.err.println("Failed to reset score on microservice: " + e.getMessage());
        }
    }

    public static int getScore() {
        try {
            return restTemplate.getForObject(BASE_URL, Integer.class);
        } catch (Exception e) {
            System.err.println("Failed to fetch score from microservice: " + e.getMessage());
            return -1;
        }
    }
}

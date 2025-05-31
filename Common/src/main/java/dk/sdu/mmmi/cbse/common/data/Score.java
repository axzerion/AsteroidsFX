package dk.sdu.mmmi.cbse.common.data;

public class Score {
    private static int points = 0;

    public static void addPoints(int value) {
        points += value;
    }

    public static int getPoints() {
        return points;
    }

    public static void resetPoints() {
        points = 0;
    }
}


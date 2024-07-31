import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HighScores {
    public static class ScoreEntry {
        String username;
        int score;

        ScoreEntry(String username, int score) {
            this.username = username;
            this.score = score;
        }

        @Override
        public String toString() {
            return username + ": " + score;
        }
    }

    public static ArrayList<ScoreEntry> getHighScores(String filename) {
        ArrayList<ScoreEntry> scores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String username = parts[0].trim();
                    int time = Integer.parseInt(parts[2].trim());
                    scores.add(new ScoreEntry(username, time));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scores;
    }

    public static ArrayList<ScoreEntry> getTopScores(String filename, int topN) {
        ArrayList<ScoreEntry> scores = getHighScores(filename);
        Collections.sort(scores, Comparator.comparingInt((ScoreEntry s) -> s.score));
        if (scores.size() > topN) {
            return new ArrayList<>(scores.subList(0, topN));
        } else {
            return scores;
        }
    }
}

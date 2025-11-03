import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import lover.Gender;

public class GameUtils {
    public static void saveScore(String username, int score) {
        Path p = Path.of("leaderboard.csv");
        // sanitize username to avoid commas/newlines in CSV
        String safeName = username == null ? "" : username.replaceAll("[\\r\\n,]+", " ");
        String entry = String.format("%s,%d%s", safeName, score, System.lineSeparator());

        try {
            Files.writeString(p, entry, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Failed to save score: " + e.getMessage());
        }
    }   

    public static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static Gender parseGenderInput(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine();

            line = line.trim();

            if (line.isEmpty()) {
                System.out.println("Please enter Male or Female.");
                continue;
            }

            String normalized = line.toLowerCase();

            if (normalized.equals("m") || normalized.equals("male")) {
                return Gender.Male;
            }

            if (normalized.equals("f") || normalized.equals("female")) {
                return Gender.Female;
            }
            
            System.out.println("Invalid gender. Please enter Male or Female.");
        }
    }

    public static List<Integer> loadScores() {
        Path p = Path.of("leaderboard.csv");
        List<Integer> scores = new ArrayList<>();
        if (!Files.exists(p)) {
            return scores;
        }

        try {
            List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line == null) continue;
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;
                String[] parts = trimmed.split(",", 2);
                if (parts.length < 2) continue;
                try {
                    int s = Integer.parseInt(parts[1].trim());
                    scores.add(s);
                } catch (NumberFormatException ignored) {
                    // skip malformed score lines
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load scores: " + e.getMessage());
        }

        return scores;
    }
}
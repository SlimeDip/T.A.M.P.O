package src;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import src.lover.*;
import src.userinterface.Gender;
import src.userinterface.User;

public class GameUtils {
    public static void saveScore(String username, int score) {
        Path p = Path.of("leaderboard.csv");
        
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

    public static List<ScoreEntry> loadLeaderboard() {
        Path p = Path.of("leaderboard.csv");
        List<ScoreEntry> entries = new ArrayList<>();
        if (!Files.exists(p)) return entries;

        try {
            List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line == null) continue;
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;
                String[] parts = trimmed.split(",", 2);
                if (parts.length < 2) continue;
                String name = parts[0].trim();
                int sc;
                try {
                    sc = Integer.parseInt(parts[1].trim());
                } catch (NumberFormatException ex) {
                    continue;
                }
                entries.add(new ScoreEntry(name, sc));
            }
        } catch (IOException e) {
            System.err.println("Failed to load leaderboard: " + e.getMessage());
        }

        entries.sort(Comparator.comparingInt(ScoreEntry::getScore)); // least -> highest
        return entries;
    }

    public static class ScoreEntry {
        private final String username;
        private final int score;

        public ScoreEntry(String username, int score) {
            this.username = username == null ? "" : username;
            this.score = score;
        }

        public String getUsername() { return username; }
        public int getScore() { return score; }
    }

    // username|gender|attractedTo|loverClass|loverName|loverGender|loverAttractedTo
    public static void saveProfiles(List<User> profiles) {
        Path p = Path.of("profiles.txt");
        List<String> lines = new ArrayList<>();
        for (User u : profiles) {
            String safeName = u.getName().replaceAll("[\r\n|]", " ");
            String gender = u.getGender().toString();
            String attracted = u.getAttractedTo().toString();
            Lover lover = u.getLover();
            String loverClass = lover.getClass().getSimpleName();
            String loverName = lover.getName().replaceAll("[\r\n|]", " ");
            String loverGender = lover.getGender().toString();
            String loverAttracted = lover.getAttractedTo().toString();
            
            String line = String.join("|", safeName, gender, attracted, loverClass, loverName, loverGender, loverAttracted);
            lines.add(line);
        }

        try {
            Files.write(p, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Failed to save profiles: " + e.getMessage());
        }
    }

    public static List<User> loadProfiles() {
        Path p = Path.of("profiles.txt");
        List<User> users = new ArrayList<>();
        if (!Files.exists(p)) return users;

        try {
            List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line == null) continue;
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;
                String[] parts = trimmed.split("\\|", -1);
                
                if (parts.length < 7) continue;
                String name = parts[0].trim();
                
                Gender g = parseGenderString(parts[1].trim());
                Gender attracted = parseGenderString(parts[2].trim());
                String loverClass = parts[3].trim();
                String loverName = parts[4].trim();
                Gender loverGender = parseGenderString(parts[5].trim());
                Gender loverAttracted = parseGenderString(parts[6].trim());

                User u = new User(name, g, attracted);

                Lover lover = null;
                if (!loverClass.isEmpty()) {
                    switch (loverClass) {
                        case "Deredere":
                            lover = new Deredere(loverName, loverGender, loverAttracted);
                            break;
                        case "Tsundere":
                            lover = new Tsundere(loverName, loverGender, loverAttracted);
                            break;
                        case "Kuudere":
                            lover = new Kuudere(loverName, loverGender, loverAttracted);
                            break;
                        case "Hot":
                            lover = new Hot(loverName, loverGender, loverAttracted);
                            break;
                        case "Chuunibyou":
                            lover = new Chuunibyou(loverName, loverGender, loverAttracted);
                            break;  
                        default:
                            // unknown class - skip creating lover
                    }
                }

                u.setLover(lover);
                users.add(u);
            }
        } catch (IOException e) {
            System.err.println("Failed to load profiles: " + e.getMessage());
        }

        return users;
    }

    private static Gender parseGenderString(String s) {
        String n = s.trim().toLowerCase();
        if (n.equals("m") || n.equals("male")) return Gender.Male;
        if (n.equals("f") || n.equals("female")) return Gender.Female;
        try { return Gender.valueOf(s); } catch (Exception ex) { return Gender.Male; }
    }
}
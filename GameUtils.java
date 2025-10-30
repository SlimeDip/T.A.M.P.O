import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameUtils {
    public static void saveScore(int score) {
        Path p = Path.of("leaderboard.json");
        String entry = String.format("{\"score\":%d}", score);

        try {
            // Creates json pag wala sa files
            if (Files.exists(p)) {
                String raw = Files.readString(p, StandardCharsets.UTF_8);
                String trimmed = raw.trim();
                if (trimmed.startsWith("[")) {
                    if (trimmed.equals("[]")) {
                        Files.writeString(p, "[" + entry + "]", StandardCharsets.UTF_8);
                    } else {
                        int lastBracket = trimmed.lastIndexOf(']');
                        if (lastBracket == -1) {
                            Files.writeString(p, "[" + entry + "]", StandardCharsets.UTF_8);
                        } else {
                            String prefix = trimmed.substring(0, lastBracket).trim();
                            if (prefix.endsWith("[")) {
                                Files.writeString(p, prefix + entry + "]", StandardCharsets.UTF_8);
                            } else {
                                Files.writeString(p, prefix + "," + entry + "]", StandardCharsets.UTF_8);
                            }
                        }
                    }
                } else if (trimmed.isEmpty()) {
                    Files.writeString(p, "[" + entry + "]", StandardCharsets.UTF_8);
                } else {
                    Files.writeString(p, "[" + entry + "]", StandardCharsets.UTF_8);
                }
            } else {
                Files.writeString(p, "[" + entry + "]", StandardCharsets.UTF_8);
            }
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
}

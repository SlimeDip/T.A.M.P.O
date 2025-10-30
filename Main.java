import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.time.Instant;

// DEVELOPMENT GUIDE BY URS TRULY DIP:
// ai.aiAnswer(List<Message> history) -> Nagbibigay ng history then return AI response

// CHANGELOG
// - Added a score saving system
// - Leaderboard is not really implemented yet (time and score lang iniistore, dapat ay user name and score lang)
// - New User class to represent players (WIP)

public class Main {
    private static void clearConsole() {
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

    private static void game() {
        Scanner input = new Scanner(System.in);
        while (true) { 
            System.out.println("=== Suyo Simulator ===");
            System.out.println("1. Start game");
            System.out.println("2. Leaderboard");
            System.out.println("3. Tutorial");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            String choice = input.nextLine();
            choice = choice.trim();
            clearConsole();

            switch (choice) {
                case "1" -> startGame(input);
                case "2" -> {
                    System.out.println("=== Leaderboard ===");
                    System.out.println("Feature coming soon!");
                    System.out.println("\nPress Enter to return to the menu...");
                    input.nextLine();
                    clearConsole();
                }
                case "3" -> {
                    System.out.println("=== Tutorial ===");
                    System.out.println("Make up with the AI lover by chatting with them. Make them say 'I love you' to win.");
                    System.out.println("Points will be awarded based on how creative your line is (WIP).");
                    System.out.println("Type 'exit' to quit the game anytime.");
                    System.out.println("Press Enter to return to the menu...");
                    input.nextLine();
                    clearConsole();
                }
                case "4" -> {
                    System.out.println("Exiting the game. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void startGame(Scanner input) {
        Ai ai = new Ai();
        int score = 0;

        try {
            String prompt = """
                You take the role of the user's lover.
                You are in a bad mood. Answer curtly and sarcastically.
                Open the conversation with a short random scenario where you are upset with the user.
                The user wins only when you genuinely forgive them; when that happens, include the exact phrase "I love you" once.
                Keep every reply between 15 and 20 words.
                """;

            List<Message> history = new ArrayList<>();
            history.add(new Message("system", prompt));

            while (true) {
                // For lover AI
                String response = ai.aiAnswer(history);
                System.out.println("Lover: " + response);
                history.add(new Message("assistant", response));
                System.out.println();

                // Check if u won
                if (response.toLowerCase().contains("i love you")) {
                    saveScore(score);
                    System.out.println("Congratulations! You've won the lover's heart!");
                    System.out.println("Score: " + score);
                    System.out.println("\nPress Enter to return to the menu...");
                    input.nextLine();
                    clearConsole();
                    return;
                }

                // For user input
                System.out.print("You: ");
                String userMessage = input.nextLine();
                userMessage = userMessage.trim();
                score++;
                if (userMessage.equalsIgnoreCase("exit")) {
                    clearConsole();
                    break;
                }

                if (userMessage.isEmpty()) {
                    System.out.println();
                } else {
                    history.add(new Message("user", userMessage));
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void saveScore(int score) {
        Path p = Path.of("leaderboard.json");
        String entry = String.format("{\"timestamp\":\"%s\",\"score\":%d}", Instant.now().toString(), score);

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


    public static void main(String[] args) {
        clearConsole();
        game();
    }
}
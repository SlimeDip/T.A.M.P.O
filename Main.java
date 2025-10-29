import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// DEVELOPMENT GUIDE BY URS TRULY DIP:
// ai.aiAnswer(List<Message> history) -> Nagbibigay ng history then return AI response

public class Main {
    private static final String WIN_MARKER = "STATE:USER_ALREADY_WON";

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
            System.out.println("2. Tutorial");
            System.out.println("3. exit");
            System.out.print("Enter your choice: ");
            String choice = input.nextLine();
            choice = choice.trim();
            clearConsole();

            switch (choice) {
                case "1" -> startGame(input);
                case "2" -> {
                    System.out.println("=== Tutorial ===");
                    System.out.println("Make up with the AI lover by chatting with them. Make them say 'I love you' to win.");
                    System.out.println("Points will be awarded based on how creative your line is (WIP).");
                    System.out.println("Type 'exit' to quit the game anytime.");
                    System.out.println("Press Enter to return to the menu...");
                    input.nextLine();
                    clearConsole();
                }
                case "3" -> {
                    System.out.println("Exiting the game. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void startGame(Scanner input) {
        Ai ai = new Ai();
        boolean hasWon = false;
        boolean winMarkerAdded = false;

        try {
            String prompt = """
                You take the role of the user's lover.
                You are in a bad mood. Answer curtly and sarcastically.
                Open the conversation with a short random scenario where you are upset with the user.
                The user wins only when you genuinely forgive them; when that happens, include the exact phrase "I love you" once.
                Check the conversation history for the system marker STATE:USER_ALREADY_WON. If it exists, the user already won, so respond warmly without repeating "I love you".
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
                if (!hasWon && response.toLowerCase().contains("i love you")) {
                    hasWon = true;
                    if (!winMarkerAdded) {
                        history.add(new Message("system", WIN_MARKER));
                        winMarkerAdded = true;
                    }
                    System.out.println("Congratulations! You've won the lover's heart!");
                    System.out.println("You can keep chatting or type 'exit' to head back to the menu.");
                    System.out.println();
                }

                // For user input
                System.out.print(hasWon ? "You (exit to menu): " : "You: ");
                String userMessage = input.nextLine();
                userMessage = userMessage.trim();
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

    public static void main(String[] args) {
        clearConsole();
        game();
    }
}
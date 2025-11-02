import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import lover.*;

// DEVELOPMENT GUIDE BY URS TRULY DIP:
// ai.aiAnswer(List<Message> history) -> Nagbibigay ng history then return AI response

// CHANGELOG
// - Added a score saving system
// - Leaderboard is not really implemented yet (time and score lang iniistore, dapat ay user name and score lang)
// - New User class to represent players (WIP)
// - New GameUtils class for utility functions

// TO DO LIST
// - Fix girlfriend and user creation then commit na sa main branch
// - Implement leaderboard viewing
// - Add Inheritance, Polymorphism
// - Inheritance: Create a difficulty class and extend it for different difficulty levels (EASY, MEDIUM, HARD) or personality types (Romantic, Sarcastic)
// - Polymorphism: Male or Female nalang siguro

public class Main {
    private static void menu(Scanner input) {
        while (true) { 
            System.out.println("=== Suyo Simulator ===");
            System.out.println("1. Start game");
            System.out.println("2. Create Character");
            System.out.println("3. Select Profile");
            System.out.println("4. Leaderboard");
            System.out.println("5. Tutorial");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            String choice = input.nextLine();
            choice = choice.trim();
            GameUtils.clearConsole();

            switch (choice) {
                case "1":
                    startGame(input);
                    break;
                case "2":
                    characterCreation(input);
                    break;
                case "3":
                    loverCreation(input);
                    break;
                case "4":
                    System.out.println("=== Leaderboard ===");
                    System.out.println("Feature coming soon!");
                    System.out.println("\nPress Enter to return to the menu...");
                    input.nextLine();
                    GameUtils.clearConsole();
                    break;
                case "5":
                    System.out.println("=== Tutorial ===");
                    System.out.println("Make up with the AI lover by chatting with them. Make them say 'I love you' to win.");
                    System.out.println("Points will be awarded based on how creative your line is (WIP).");
                    System.out.println("Type 'exit' to quit the game anytime.");
                    System.out.println("Press Enter to return to the menu...");
                    input.nextLine();
                    GameUtils.clearConsole();
                    break;
                case "6":
                    System.out.println("Exiting the game. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    GameUtils.clearConsole();
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
                    GameUtils.saveScore(score);
                    System.out.println("Congratulations! You've won the lover's heart!");
                    System.out.println("Score: " + score);
                    System.out.println("\nPress Enter to return to the menu...");
                    input.nextLine();
                    GameUtils.clearConsole();
                    return;
                }

                // For user input
                System.out.print("You: ");
                String userMessage = input.nextLine();
                userMessage = userMessage.trim();
                score++;
                if (userMessage.equalsIgnoreCase("exit")) {
                    GameUtils.clearConsole();
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

    private static void characterCreation(Scanner input) {
        System.out.println("=== Character Creation ===");
        System.out.print("Enter your name: ");
        String name = input.nextLine().trim();

        System.out.print("Enter your gender (Male/Female): ");
        Gender gender = Gender.valueOf(input.nextLine().trim());

        System.out.print("Enter your what gender are you attracted to? (Male/Female): ");
        Gender attractedTo = Gender.valueOf(input.nextLine().trim());

        User user = new User(name, gender, attractedTo);
    }

    private static void loverCreation(Scanner input) {
        System.out.println("=== Lover Creation ===");
        System.out.print("Enter lover's name: ");
        String name = input.nextLine().trim();

        System.out.print("Enter lover's gender (Male/Female): ");
        Gender gender = Gender.valueOf(input.nextLine().trim());

        System.out.print("Enter lover's attractedTo: ");
        Gender attractedTo = Gender.valueOf(input.nextLine().trim());

        System.out.print("Select lover's personality type: ");
        System.out.print("1. Default");
        System.out.print("2. Hostile and cold (Tsundere) ");
        System.out.print("3. Sweet and caring (Deredere) ");
        System.out.print("4. Emotionless and aloof (Kuudere) ");
        System.out.print("Enter your choice: ");
        int personalityChoice = 1;

        try {
            personalityChoice = input.nextInt();
        } catch (Exception e) {
            System.out.print("Invalid input. Defaulting to 1: ");
            personalityChoice = 1;
        }

        Lover lover;
        switch (personalityChoice) {
            case 1:
                lover = new Lover(name, gender, attractedTo);
                break;
            case 2:
                lover = new Tsundere(name, gender, attractedTo);
                break;
            case 3:
                lover = new Deredere(name, gender, attractedTo);
                break;
            case 4:
                lover = new Kuudere(name, gender, attractedTo);
                break;
            default:
                System.out.println("Invalid choice. Choosing Default personality.");
                lover = new Lover(name, gender, attractedTo);
        }

        System.out.println("Lover created: " + lover.getName() + " (" + lover.getPrompt() + ")");
        System.out.println("Press Enter to return to menu...");
        input.nextLine();
        GameUtils.clearConsole();

    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        GameUtils.clearConsole();
        characterCreation(input);
        menu(input);
    }
}
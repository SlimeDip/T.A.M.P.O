package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import src.lover.*;
import src.userinterface.*;


// DEVELOPMENT GUIDE BY URS TRULY DIP:
// ai.aiAnswer(List<Message> history) -> Nagbibigay ng history then return AI response
// (variable) profiles -> List of User profiles
// (variable) currentUser -> The currently selected User profile
// lover folder -> Contains Lover classes with different personalities
// GameUtils -> Utility class for common functions like clearing console and saving scores
// Flex ko lang pero I debugged 70 bugs while making this, napaka ez

// When adding new Lover personalities, create a new class in lover/ extending Lover and implement getPrompt()
// Then, add a case in loverCreation() switch statement to create an instance of the new
// Also edit loadProfiles() and saveProfiles() in GameUtils.java to support the new Lover class


// CHANGELOG
// - Added a score saving system
// - Leaderboard is not really implemented yet (time and score lang iniistore, dapat ay user name and score lang)
// - New User class to represent players (WIP)
// - New GameUtils class for utility functions
// - Cleaner menu system
// - Added character and lover creation flow
// - Added personality types for lovers (Hot, Tsundere, Deredere, Kuudere)
// - Improved AI prompt system based on lover personality


// TO DO LIST
// - Implement leaderboard viewing
// - Improve AI prompt system (try more personality types)
// - Kung kaya pa ay local saving ng profiles (file system)

public class Main {
    private static final List<User> profiles = new ArrayList<>();
    private static User currentUser = null;

    private static void menu(Scanner input) {
        while (true) { 
            System.out.println("=== Suyo Simulator ===");
            System.out.println("Current Profile: " + currentUser.getName());
            System.out.println("1. Start game");
            System.out.println("2. Create New Profile");
            System.out.println("3. Select Profile");
            System.out.println("4. Leaderboard");
            System.out.println("5. Tutorial");
            System.out.println("6. Save and Exit");
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
                    viewProfiles(input);
                    break;
                case "4":
                    viewLeaderboard(input);
                    break;
                case "5":
                    System.out.println("=== Tutorial ===");
                    System.out.println("Make up with the AI lover by chatting with them. Make them say 'I love you' to win.");
                    System.out.println("Every message you enter gives you a score.");
                    System.out.println("The lower your score when you win, the better!");
                    System.out.println("Type 'exit' to quit the game anytime.");
                    System.out.println("Press Enter to return to the menu...");
                    input.nextLine();
                    GameUtils.clearConsole();
                    break;
                case "6":
                    System.out.println("Exiting the game. Goodbye!");
                    GameUtils.saveProfiles(profiles);
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
            String prompt = currentUser.getLover().getPrompt();

            List<Message> history = new ArrayList<>();
            history.add(new Message("system", prompt));

            while (true) {
                // For lover AI
                String response = ai.aiAnswer(history);
                System.out.println(currentUser.getLover().getName() + ": " + response);
                history.add(new Message("assistant", response));
                System.out.println();

                // Check if u won
                if (response.toLowerCase().contains("i love you")) {
                    GameUtils.saveScore(currentUser.getName(), score);
                    System.out.println("Congratulations! You've won the lover's heart!");
                    System.out.println("Score: " + score);
                    System.out.println("\nPress Enter to return to the menu...");
                    input.nextLine();
                    GameUtils.clearConsole();
                    return;
                }

                // For user input
                System.out.print(currentUser.getName() + ": ");
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

        Gender gender = GameUtils.parseGenderInput(input, "Enter your gender (Male/Female): ");

        Gender attractedTo = GameUtils.parseGenderInput(input, "Enter the gender you are attracted to (Male/Female): ");

        System.out.println("\nCharacter created: " + name + " (" + gender + ", attracted to " + attractedTo + ")");
        System.out.println("Press Enter to proceed to lover creation...");
        input.nextLine();
        User user = new User(name, gender, attractedTo);
        GameUtils.clearConsole();
        loverCreation(input, user);
    }

    private static void loverCreation(Scanner input, User user) {
        System.out.println("=== Lover Creation ===");
        System.out.print("Enter lover's name: ");
        String name = input.nextLine().trim();
        System.out.println("Pick your Lover's Language");
        System.out.println("1. English");
        System.out.println("2. Tagalog");
        System.out.println("3. Bisaya");
        
        String choice = input.nextLine();
        Language language;
        switch (choice) {
            case "1":
                language = Language.ENGLISH;
                break;
            case "2":
                language = Language.TAGALOG;
                break;
            case "3":
                language = Language.BISAYA;
                break;
            default:
                language = Language.ENGLISH;
                break;
        }
        

        Gender gender = user.getAttractedTo();
        Gender attractedTo = user.getGender();

        System.out.println("\nSelect lover's personality type:");
        System.out.println("1. Hot and passionate (Default)");
        System.out.println("2. Hostile and cold (Tsundere)");
        System.out.println("3. Sweet and caring (Deredere)");
        System.out.println("4. Emotionless and aloof (Kuudere)");
        System.out.println("5. Delusional and dramatic (Chuunibyou)");
        System.out.println("6. Swagger and attitude (YoungStunna)");
        System.out.print("Enter your choice: ");
        int personalityChoice = 1;
        String personalityType = "Default";

        try {
            personalityChoice = input.nextInt();
            input.nextLine();
        } catch (Exception e) {
            System.out.print("Invalid input. Defaulting to 1: ");
            personalityChoice = 1;
        }

        Lover lover;
        switch (personalityChoice) {
            case 1:
                lover = new Hot(name, gender, attractedTo, language);
                personalityType = "Hot";
                break;
            case 2:
                lover = new Tsundere(name, gender, attractedTo, language);
                personalityType = "Tsundere";
                break;
            case 3:
                lover = new Deredere(name, gender, attractedTo, language);
                personalityType = "Deredere";
                break;
            case 4:
                lover = new Kuudere(name, gender, attractedTo, language);
                personalityType = "Kuudere";
                break;
            case 5:
                lover = new Chuunibyou(name, gender, attractedTo, language);
                personalityType = "Chuunibyou";
                break;
            case 6:
                lover = new YoungStunna(name, gender, attractedTo, language);
                personalityType = "YoungStunna";
                break;
            default:
                System.out.println("Invalid choice. Choosing Default personality.");
                lover = new Hot(name, gender, attractedTo, language);
        }

        user.setLover(lover);
        profiles.add(user);
        currentUser = user;
        
        GameUtils.saveProfiles(profiles);

        System.out.println("\nLover created: " + name + " (" + gender + ", attracted to " + attractedTo + ")");
        System.out.println("Personality type: " + personalityType);
        System.out.println("\nLover Creation complete! Press Enter to return to menu...");
        input.nextLine();
        GameUtils.clearConsole();
    }

    private static void viewProfiles(Scanner input) {
        int num = 1;
        for (User user : profiles) {
            System.out.print(num + ". User: " + user.getName() + " (" + user.getGender() + ", attracted to " + user.getAttractedTo() + ")");
            Lover lover = user.getLover();
            System.out.println(" | Lover: " + lover.getName() + " (" + lover.getGender() + ", attracted to " + lover.getAttractedTo() + ")");
            num++;
        }

        System.out.print("\nSelect profile to use: ");
        int profileIndex = input.nextInt() - 1;
        input.nextLine();

        if (profileIndex >= 0 && profileIndex < profiles.size()) {
            currentUser = profiles.get(profileIndex);
            System.out.println("Profile selected: " + currentUser.getName());
        } else {
            System.out.println("Invalid profile selection.");
        }
        GameUtils.clearConsole();
    }

    private static void viewLeaderboard(Scanner input) {
        System.out.println("=== Leaderboard ===");
        List<GameUtils.ScoreEntry> entries = GameUtils.loadLeaderboard();
        if (entries.isEmpty()) {
            System.out.println("No scores yet.");
        } else {
            int rank = 1;
            for (GameUtils.ScoreEntry e : entries) {
                System.out.println(rank + ". " + e.getUsername() + " - " + e.getScore());
                rank++;
            }
        }
        System.out.println("\nPress Enter to return to the menu...");
        input.nextLine();
        GameUtils.clearConsole();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        GameUtils.clearConsole();
        
        List<User> loaded = GameUtils.loadProfiles();
        if (loaded != null && !loaded.isEmpty()) {
            profiles.addAll(loaded);
            currentUser = profiles.get(0);
        } else {
            characterCreation(input);
        }

        menu(input);
    }
}
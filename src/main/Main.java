package src.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import src.display.ConsoleArt.Emotion;
import src.lover.*;
import src.util.*;

// DEVELOPMENT GUIDE BY URS TRULY DIP:
// Ai.chatWithAnalysis(List<Message> history) -> returns assistant reply + analysis
// (variable) profiles -> List of User profiles
// (variable) currentUser -> The currently selected User profile
// lover folder -> Contains Lover classes with different personalities
// GameUtils -> Utility class for common functions like clearing console and saving scores
// Flex ko lang pero I debugged 70 bugs while making this, napaka ez

// When adding new Lover personalities, create a new class in lover/ extending Lover and implement getPrompt()
// Then, add a case in loverCreation() switch statement to create an instance of the new
// Also edit loadProfiles() and saveProfiles() in GameUtils.java to support the new Lover class

// TO DO LIST
// - Improve AI prompt system (try more personality types)

public class Main {
    private static List<User> profiles = new ArrayList<>();
    private static User currentUser = null;

    private static void menu(Scanner input) {
        while (true) { 
            System.out.println("=== Suyo Simulator ===");
            System.out.println("Current Profile: " + currentUser.getName());
            System.out.println("1. Start game");
            System.out.println("2. Profile Settings");
            System.out.println("3. Leaderboard");
            System.out.println("4. Tutorial");
            System.out.println("5. Save and Exit");
            System.out.print("\nEnter your choice: ");
            String choice = input.nextLine();
            choice = choice.trim();
            GameUtils.clearConsole();

            switch (choice) {
                case "1":
                    startGame(input);
                    break;
                case "2":
                    profileSettings(input);
                    break;
                case "3":
                    viewLeaderboard(input);
                    break;
                case "4":
                    Tutorial(input);
                    break;
                case "5":
                    System.out.println("Exiting the game. Goodbye!");
                    GameUtils.saveProfiles(profiles);
                    System.exit(0);
                    break;
                default:
                    GameUtils.clearConsole();
            }
        }
    }

    private static void Tutorial(Scanner input){
        System.out.println("=== Tutorial ===");
        System.out.println("\u001B[32m" + "Make up with the AI lover by chatting with them.");
        System.out.println("The AI will provide a random scenario where he/she is upset with you." + "\u001B[0m");
        System.out.print("\nPress Enter to continue...");
        input.nextLine();
        GameUtils.clearConsole();

        currentUser.getLover().displayWithEmotion("You just forgot our anniversary, and now you're trying to make it up to me with some half-hearted excuse, how nice.", Emotion.ANGRY);
        System.out.println(currentUser.getName() + ": I'm Sorry my love, I promise it won't happen again.\n");
        System.out.println("\u001B[32m" + "\t\t^^your reply^^");
        System.out.println("Every reply you enter gives you a score.");
        System.out.println("Type " + "\u001B[31m" + "'exit'" + "\u001B[32m" + " to quit the game anytime." + "\u001B[0m");
        System.out.print("\nPress Enter to continue...");
        input.nextLine();
        GameUtils.clearConsole();

        currentUser.getLover().displayWithEmotion("I will forgive you this time, but expect that there will be no second chance next time.", Emotion.HAPPY);
        System.out.println("Congratulations! You've won the lover's heart!");
        System.out.println("\u001B[38;2;255;215;0m" + "Score: 1");
        System.out.println("\u001B[32m" + "The fewer reply the better!");
        System.out.println("That just means you're a great mood patcher!" + "\u001B[0m");
        System.out.print("\nPress Enter to return to the menu...");
        input.nextLine();
        GameUtils.clearConsole();
    }

    private static void startGame(Scanner input) {
    int score = 0;

    try {
        String prompt = currentUser.getLover().getPrompt();
        List<Message> history = new ArrayList<>();
        List<String> conversationHistory = new ArrayList<>(); 
        history.add(new Message("system", prompt));

        while (true) {
            GameUtils.clearConsole();
            
            // Print conversation history
            for (String line : conversationHistory) {
                System.out.println(line);
            }
            System.out.println("=" .repeat(50)); 
            
            Ai.ChatResponse ai = Ai.chatWithAnalysis(history);
            String response = ai.content;
            Emotion emotion = ai.mood;

            // Display lover with emotion and art
            currentUser.getLover().displayWithEmotion(response, emotion);
            
            // Add to conversation history
            conversationHistory.add(currentUser.getLover().getName() + ": " + response);
            history.add(new Message("assistant", response));

            if (ai.forgiven) {
                GameUtils.saveScore(currentUser.getName(), score);
                System.out.println("\nCongratulations! You've won the lover's heart!");
                System.out.println("\u001B[38;2;255;215;0m" + "Score: " + score + "\u001B[0m");
                System.out.println("\nPress Enter to return to the menu...");
                input.nextLine();
                GameUtils.clearConsole();
                return;
            }

            System.out.print("\n" + currentUser.getName() + ": ");
            String userMessage = input.nextLine();
            userMessage = userMessage.trim();
            score++;

            if (userMessage.equalsIgnoreCase("exit")) {
                GameUtils.clearConsole();
                break;
            }

            if (userMessage.isEmpty()) {
                history.add(new Message("user", "..."));
                conversationHistory.add(currentUser.getName() + ": ...");
            } else {
                history.add(new Message("user", userMessage));
                conversationHistory.add(currentUser.getName() + ": " + userMessage);
            }
        }
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}

    private static void profileSettings(Scanner input) {
        while (true) {
            System.out.println("=== Profile Settings ===");
            System.out.println("Current Profile: " + currentUser.getName());
            System.out.println("1. Create New Profile");
            System.out.println("2. Delete Profile");
            System.out.println("3. Select Profile");
            System.out.println("4. Exit");
            System.out.print("\nEnter your choice: ");
            String choice = input.nextLine().trim();
            GameUtils.clearConsole();

            switch (choice) {
                case "1":
                    characterCreation(input);
                    break;
                case "2":
                    deleteProfile(input);
                    break;
                case "3":
                    viewProfiles(input);
                    break;
                case "4":
                    return;
                default:
                    GameUtils.clearConsole();
                    return;        
            }
        }
    }

    private static void characterCreation(Scanner input) {
        System.out.println("=== Character Creation ===");

        System.out.print("Enter your name: ");
        String name = input.nextLine().trim();
        if (name.isEmpty()) {
            name = "User";
        }

        System.out.print("Enter your gender (Male/Female): ");
        Gender gender = GameUtils.parseGenderString(input.nextLine().trim());

        System.out.print("Enter the gender you are attracted to (Male/Female): ");
        Gender attractedTo = GameUtils.parseGenderString(input.nextLine().trim());

        System.out.println("\nCharacter created: " + name + " (" + gender + ", attracted to " + attractedTo + ")");
        System.out.println("Press Enter to proceed to lover creation...");
        input.nextLine();
        User user = new User(name, gender, attractedTo);
        GameUtils.clearConsole();
        loverCreation(input, user);
    }

    private static void deleteProfile(Scanner input) {
        System.out.println("=== Delete Profile ===");
        int num = 1;
        for (User user : profiles) {
            if (num % 2 == 0) {
                System.out.print("\u001b[38;5;229m");
            } else {
                System.out.print("\u001B[0m");
            }
            System.out.println(num + ". " + user.getName());
            num++;
        }

        System.out.print("\u001B[0m");
        System.out.println("\nType exit to cancel deletion.");

        System.out.print("Select profile to delete: ");
        String inputStr = input.nextLine().trim();
        if (inputStr.equalsIgnoreCase("exit")) {
            GameUtils.clearConsole();
            return;
        }
        int profileIndex = Integer.parseInt(inputStr) - 1;
        if (profileIndex == -1) {
            GameUtils.clearConsole();
            return;
        }

        if (profileIndex >= 0 && profileIndex < profiles.size()) {
            User profileToDelete = profiles.get(profileIndex);
            System.out.println("Are you sure you want to delete '" + profileToDelete.getName() + "'?");
            System.out.print("Type 'yes' to confirm or 'no' to cancel: ");
            String confirm = input.nextLine().trim().toLowerCase();

            if (confirm.equals("yes")) {
                String deletedName = profileToDelete.getName();
                profiles.remove(profileIndex);
                GameUtils.saveProfiles(profiles);

                if (currentUser.getName().equals(deletedName)) {
                    if (profiles.isEmpty()) {
                        System.out.println("Profile '" + deletedName + "' deleted successfully.");
                        System.out.println("No profiles remaining. Creating new profile...");
                        System.out.println("Press Enter to continue...");
                        input.nextLine();
                        GameUtils.clearConsole();
                        characterCreation(input);
                        currentUser = profiles.get(0);
                    } else {
                        currentUser = profiles.get(0);
                        System.out.println("Profile '" + deletedName + "' deleted successfully.");
                        System.out.println("Switched to profile: " + currentUser.getName());
                        System.out.println("Press Enter to return to menu...");
                        input.nextLine();
                    }
                } else {
                    System.out.println("Profile '" + deletedName + "' deleted successfully.");
                    System.out.println("Press Enter to return to menu...");
                    input.nextLine();
                }
            } 
            
            else if (confirm.equals("no")){
                System.out.println("Deletion cancelled.");
                System.out.println("Press Enter to return to menu...");
                input.nextLine();
            }
        } else {
            System.out.println("Invalid profile selection.");
            System.out.println("Press Enter to return to menu...");
            input.nextLine();
        }
        GameUtils.clearConsole();
    }

    private static void loverCreation(Scanner input, User user) {
        System.out.println("=== Lover Creation ===");
        System.out.print("Enter lover's name: ");
        String name = input.nextLine().trim();
        if (name.isEmpty()) {
            name = "Lover";
        }
        
        System.out.println("\nPick your Lover's Language");
        System.out.println("1. English");
        System.out.println("2. Tagalog");
        System.out.println("3. Bisaya");
        System.out.println("4. Cebuano");
        System.out.print("Enter your choice: ");
        
        String choice = input.nextLine().trim();
        Language language;
        switch (choice) {
            case "1":
                language = Language.English;
                break;
            case "2":
                language = Language.Tagalog;
                break;
            case "3":
                language = Language.Bisaya;
                break;
            case "4":
                language = Language.Cebuano;
                break;
            default:
                language = Language.English;
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
        System.out.println("6. Swagger and attitude (Young Stunna)");
        System.out.println("7. Shy and soft-spoken (Timid)");
        System.out.print("Enter your choice: ");
        String personalityChoice = input.nextLine();
        
        Lover lover;
        switch (personalityChoice) {
            case "1":
                lover = new Passionate(name, gender, attractedTo, language);
                break;
            case "2":
                lover = new Tsundere(name, gender, attractedTo, language);
                break;
            case "3":
                lover = new Deredere(name, gender, attractedTo, language);
                break;
            case "4":
                lover = new Kuudere(name, gender, attractedTo, language);
                break;
            case "5":
                lover = new Chuunibyou(name, gender, attractedTo, language);
                break;
            case "6":
                lover = new YoungStunna(name, gender, attractedTo, language);
                break;
            case "7":
                lover = new Timid(name, gender, attractedTo, language);
                break;
            default:
                System.out.println("Invalid choice. Choosing Default personality.");
                lover = new Passionate(name, gender, attractedTo, language);
        }

        user.setLover(lover);
        profiles.add(user);
        currentUser = user;
        
        GameUtils.saveProfiles(profiles);

        System.out.println("\nLover created: " + name + " (" + gender + ", attracted to " + attractedTo + ")");
        System.out.println("Personality type: " + currentUser.getLover().getClass().getSimpleName());
        System.out.println("\nLover Creation complete! Press Enter to return to menu...");
        input.nextLine();
        GameUtils.clearConsole();
    }

    private static void viewProfiles(Scanner input) {
        int num = 1;
        for (User user : profiles) {
            if (num % 2 == 0) {
                System.out.print("\u001b[38;5;229m");
            } else {
                System.out.print("\u001B[0m");
            }
            System.out.print(num + ". User: " + user.getName());
            Lover lover = user.getLover();
            System.out.println(" | Lover: " + lover.getName() + " (" + lover.getClass().getSimpleName() + ") | Language: " + lover.getLanguage());
            num++;
        }

        System.out.print("\u001B[0m");
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

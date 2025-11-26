package src.display;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConsoleArt {
    private static final String BLOCK = "██";
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001b[36m";
    private static final String YELLOW = "\u001b[33m";
    
    public enum Emotion {
        NEUTRAL, HAPPY, DISAPPOINTED, ANGRY, POUTING, POUTING2
    }


    private static final int ART_BLOCK_WIDTH = 16;
    private static final int ART_HEIGHT = 12;
    private static final int DIALOGUE_WRAP_WIDTH = 65; 


    /**
     * Get emotion name for display
     */
    public static String getEmotionName(Emotion emotion) {
        switch (emotion) {
            case NEUTRAL: return "NEUTRAL";
            case HAPPY: return "HAPPY";
            case DISAPPOINTED: return "DISAPPOINTED";
            case ANGRY: return "ANGRY";
            case POUTING: return "POUTING";
            case POUTING2: return "POUTING2";
            default: return "NEUTRAL";
        }
    }

    private static List<String> splitDialogue(String text) {
        String[] paragraphLines = text.split("\n");
        List<String> wrappedLines = new ArrayList<>();

        for (String line : paragraphLines) {
            List<String> words = Arrays.asList(line.trim().split("\\s+"));
            
            List<String> lines = words.stream()
                .reduce(new ArrayList<String>(), (linesAccumulator, word) -> {
                    if (linesAccumulator.isEmpty() || linesAccumulator.get(linesAccumulator.size() - 1).length() + word.length() + 1 > DIALOGUE_WRAP_WIDTH) {
                        linesAccumulator.add(word);
                    } else {
                        int lastIndex = linesAccumulator.size() - 1;
                        linesAccumulator.set(lastIndex, linesAccumulator.get(lastIndex) + " " + word);
                    }
                    return linesAccumulator;
                }, (lines1, lines2) -> {
                    lines1.addAll(lines2);
                    return lines1;
                });
            wrappedLines.addAll(lines);
        }
        
        return wrappedLines;
    }
    
    public static void displayArt(String loverName, String dialogue, Emotion emotion, String[][] emotionPixels) {
        List<String> wrappedDialogue = splitDialogue(dialogue);
        int totalLines = Math.max(ART_HEIGHT, wrappedDialogue.size());
        
        String ART_SPACE_PADDING = " ".repeat(ART_BLOCK_WIDTH * 2 + 3);
        
        System.out.println(CYAN + "\n--- " + loverName.toUpperCase() + " (" + getEmotionName(emotion) + ") ---" + RESET);

        for (int i = 0; i < totalLines; i++) {
            StringBuilder line = new StringBuilder();
            //Print the art part
            if (i < ART_HEIGHT) {
                for (String color : emotionPixels[i]) {
                    line.append(color).append(BLOCK);
                }
                line.append(RESET);
                line.append("   "); 
            } else {
                line.append(ART_SPACE_PADDING);
            }
            // Append the dialogue line
            String dialogueLine = (i < wrappedDialogue.size()) ? wrappedDialogue.get(i) : "";
            line.append(YELLOW).append(dialogueLine).append(RESET);
            
            System.out.println(line.toString());
        } 
    }
}
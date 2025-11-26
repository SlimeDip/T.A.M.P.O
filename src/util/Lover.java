package src.util;

import src.display.ConsoleArt;

public abstract class Lover {
    private String name;
    private Gender gender;
    private Gender attractedTo;
    private Language language;

    public Lover(String name, Gender gender, Gender attractedTo, Language language) {
        this.name = name;
        this.gender = gender;
        this.attractedTo = attractedTo;
        this.language = language;
    }

    public String getName() { return name; }
    public Gender getGender() { return gender; }
    public Gender getAttractedTo() { return attractedTo; }
    public Language getLanguage() {return language;}

    public abstract String getPrompt();
    public abstract String[][] getPixelsForEmotion(ConsoleArt.Emotion emotion);
    public abstract void displayWithEmotion(String dialogue, ConsoleArt.Emotion emotion);
}
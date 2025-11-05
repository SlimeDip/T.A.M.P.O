package src.lover;

import src.userinterface.Gender;

public abstract class Lover {
    private String name;
    private Gender gender;
    private Gender attractedTo;

    public Lover(String name, Gender gender, Gender attractedTo) {
        this.name = name;
        this.gender = gender;
        this.attractedTo = attractedTo;
    }

    public String getName() { return name; }
    public Gender getGender() { return gender; }
    public Gender getAttractedTo() { return attractedTo; }

    public void setName(String name) { this.name = name; }
    public void setGender(Gender gender) { this.gender = gender; }
    public void setAttractedTo(Gender attractedTo) { this.attractedTo = attractedTo; }

    public abstract String getPrompt();
}
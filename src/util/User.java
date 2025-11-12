package src.util;

public class User {
    private String name;
    private Gender gender;
    private Gender attractedTo;
    private Lover lover;
    private int score;
   

    public User(String name, Gender gender, Gender attractedTo) {
        this.name = name;
        this.gender = gender;
        this.attractedTo = attractedTo;
        this.score = 0;
    }

    public String getName() { return name;}
    public Gender getGender() { return gender;}
    public Gender getAttractedTo() { return attractedTo; }
    public int getScore() { return score; }
    public Lover getLover() { return lover; }
    
    public void setLover(Lover lover) { this.lover = lover; }
}

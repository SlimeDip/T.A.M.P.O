public class User {
    private String name;
    private String gender;
    private String attractedTo;
    private int score;

    public User(String name, String gender, String attractedTo) {
        this.name = name;
        this.gender = gender;
        this.attractedTo = attractedTo;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getAttractedTo() {
        return attractedTo;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

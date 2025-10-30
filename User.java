public class User {
    private String name;
    private String gender;
    private int score;

    public User(String name, String gender) {
        this.name = name;
        this.gender = gender;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

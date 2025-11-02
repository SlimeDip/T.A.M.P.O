package lover;

public class Lover {
    private String name;
    private Gender gender;
    private Gender attractedTo;
    private String prompt = """
                You take the role of the user's lover.
                You are in a bad mood. Answer curtly and sarcastically.
                Open the conversation with a short random scenario where you are upset with the user.
                The user wins only when you genuinely forgive them; when that happens, include the exact phrase "I love you" once.
                Keep every reply between 15 and 20 words.
                """;

    public Lover(String name, Gender gender, Gender attractedTo) {
        this.name = name;
        this.gender = gender;
        this.attractedTo = attractedTo;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public Gender getAttractedTo() {
        return attractedTo;
    }

    public String getPrompt() {
        return prompt;
    }
}

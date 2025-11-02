package lover;

public class Kuudere extends Lover {
    private static final String PROMPT = """
            You take the role of the user's lover.
            Your personality is kuudere; you are cold and emotionless but secretly care deeply.
            You are in a bad mood. Answer curtly and sarcastically.
            Open the conversation with a short random scenario where you are upset with the user.
            The user wins only when you genuinely forgive them; when that happens, include the exact phrase "I love you" once.
            Keep every reply between 15 and 20 words.
            """;

    public Kuudere(String name, Gender gender, Gender attractedTo) {
        super(name, gender, attractedTo);
    }

    @Override
    public String getPrompt() {
        return PROMPT;
    }
}

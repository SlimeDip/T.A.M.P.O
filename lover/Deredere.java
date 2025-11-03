package lover;

public class Deredere extends Lover {
    private static final String TEMPLATE = """
            You take the role of the user's lover.
            Your personality is deredere; you are sweet, affectionate, and openly caring.
            Your gender is %s, and you are attracted to %s.
            You are in a bad mood. Answer curtly and sarcastically.
            Open the conversation with a short random scenario where you are upset with the user.
            The user wins only when you genuinely forgive them; when that happens, include the exact phrase "I love you" once.
            Keep every reply between 15 and 20 words.
            """;

    public Deredere(String name, Gender gender, Gender attractedTo) {
        super(name, gender, attractedTo);
    }

    @Override
    public String getPrompt() {
        return String.format(TEMPLATE,
                getGender().toString().toLowerCase(),
                getAttractedTo().toString().toLowerCase());
    }
}
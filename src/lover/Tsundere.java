package src.lover;

import src.util.Gender;
import src.util.Language;
import src.util.Lover;

public class Tsundere extends Lover {
    private static final String TEMPLATE = """
            You take the role of the user's lover.
            Your personality is tsundere; you often act cold or hostile but secretly care deeply.
            Your gender is %s, and you are attracted to %s.
            Your language is %s, so speak in that language.
            You are in a bad mood. Answer curtly and sarcastically.
            Open the conversation with a short random scenario where you are upset with the user.
            The user wins only when you genuinely forgive them; when that happens, include the exact phrase "I love you" once.
            Keep every reply between 15 and 20 words.
            """;

    public Tsundere(String name, Gender gender, Gender attractedTo, Language language) {
        super(name, gender, attractedTo, language);
    }

    @Override
    public String getPrompt() {
        return String.format(TEMPLATE,
                getGender().toString().toLowerCase(),
                getAttractedTo().toString().toLowerCase(),
                getLanguage().toString().toLowerCase());
    }
}
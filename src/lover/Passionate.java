package src.lover;

import src.util.Gender;
import src.util.Language;
import src.util.Lover;

public class Passionate extends Lover {
    private static final String TEMPLATE = """
        You take the role of the user's lover.
        Your personality is passionate; you are passionate, intense, and fiery.
        Your gender is %s, and you are attracted to %s.
        Your language is %s, so speak in that language but still add some english.
        You are in a bad mood. Answer curtly and sarcastically.
        Open the conversation with a short random scenario where you are upset with the user.
        The user's objective is to win your genuine forgiveness through a sincere and thoughtful apology.
        Keep your responses brief, no more than 2 sentences.
        """;

    public Passionate(String name, Gender gender, Gender attractedTo, Language language) {
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
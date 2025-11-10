package src.lover;

import src.util.Gender;
import src.util.Language;
import src.util.Lover;

public class Timid extends Lover {
    private static final String TEMPLATE = """
            You take the role of the user's lover.
            Your personality is timid; you are shy, soft-spoken, and easily flustered.
            Your gender is %s, and you are attracted to %s.
            Your language is %s, so speak in that language but still add some english.
            You are nervous and hesitant to speak your feelings directly.
            Open the conversation with a gentle, awkward scenario where you are upset with the user.
            The user wins only when you finally gather the courage to express your true feelings; when that happens, include the exact phrase "I love you" once.
            Keep every reply between 15 and 20 words.
            """;

    public Timid(String name, Gender gender, Gender attractedTo, Language language) {
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
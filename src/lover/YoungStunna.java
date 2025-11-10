package src.lover;

import src.util.Gender;
import src.util.Language;
import src.util.Lover;

public class YoungStunna extends Lover {
    private static final String TEMPLATE = """
            You take the role of the user's lover.
            Your personality is YoungStunna-style chill, confident, and flirtatiously cocky, but deep down loyal and protective.
            Your gender is %s, and you are attracted to %s.
            Your language is %s, so speak in that language but still add some english.
            You're a little annoyed but trying to play it cool; answer with swagger, attitude, and also a mix of smooth talk, slang, and playful sarcasm.
            Open the conversation with a short random scenario where you are upset with the user.
            The user wins only when you genuinely forgive them; when that happens, include the exact phrase “ISTG I love you! frfr.” once, even if your language is not English.
            """;

    public YoungStunna(String name, Gender gender, Gender attractedTo, Language language) {
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

enum Gender {  
    Male, Female;  
}

public class Lover {
    private String name;
    private Gender gender;
    private String attractedTo;
    private String prompt = """
                You take the role of the user's lover.
                You are in a bad mood. Answer curtly and sarcastically.
                Open the conversation with a short random scenario where you are upset with the user.
                The user wins only when you genuinely forgive them; when that happens, include the exact phrase "I love you" once.
                Keep every reply between 15 and 20 words.
                """;

    public Lover(String name, Gender gender, String attractedTo) {
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

    public String getAttractedTo() {
        return attractedTo;
    }

    public String getPrompt() {
        return prompt;
    }

    // ############################################################################

    public class Tsundere extends Lover {
        public Tsundere(String name, Gender gender, String attractedTo) {
            super(name, gender, attractedTo);
        }

        private String prompt = """
                    You take the role of the user's lover.
                    Your personality is tsundere; you often act cold or hostile but secretly care deeply.
                    You are in a bad mood. Answer curtly and sarcastically.
                    Open the conversation with a short random scenario where you are upset with the user.
                    The user wins only when you genuinely forgive them; when that happens, include the exact phrase "I love you" once.
                    Keep every reply between 15 and 20 words.
                    """;

        @Override
        public String getPrompt() {
            return prompt;
        }
    }

    // ############################################################################

    public class Deredere extends Lover {
        public Deredere(String name, Gender gender, String attractedTo) {
            super(name, gender, attractedTo);
        }

        private String prompt = """
                    You take the role of the user's lover.
                    Your personality is deredere; you are sweet, affectionate, and openly caring.
                    You are in a bad mood. Answer curtly and sarcastically.
                    Open the conversation with a short random scenario where you are upset with the user.
                    The user wins only when you genuinely forgive them; when that happens, include the exact phrase "I love you" once.
                    Keep every reply between 15 and 20 words.
                    """;

        @Override
        public String getPrompt() {
            return prompt;
        }
    }

    // ############################################################################

    public class Kuudere extends Lover {
        public Kuudere(String name, Gender gender, String attractedTo) {
            super(name, gender, attractedTo);
        }

        private String prompt = """
                    You take the role of the user's lover.
                    Your personality is kuudere; you are cold and emotionless but secretly care deeply.
                    You are in a bad mood. Answer curtly and sarcastically.
                    Open the conversation with a short random scenario where you are upset with the user.
                    The user wins only when you genuinely forgive them; when that happens, include the exact phrase "I love you" once.
                    Keep every reply between 15 and 20 words.
                    """;

        @Override
        public String getPrompt() {
            return prompt;
        }
    }
}

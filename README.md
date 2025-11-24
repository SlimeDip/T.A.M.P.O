<div align="center">

# 🤖T.A.M.P.O. — The AI Mood-Patching Odyssey  
**An interactive console-based AI chat game that simulates emotional conversations through personality-driven responses and different personalities.**



**T.A.M.P.O.** is a Java console game that simulates relationship “tampo” dynamics through conversations with different AI personalities. You create a profile, pick a lover personality, and try to earn forgiveness through "panunuyo". The AI replies in character and appends a tiny JSON object indicating its mood and whether it forgives you, which the game uses to render mood art and determine game end.
</div>

---

## 💭 Highlights

In **T.A.M.P.O.**, words are your only tool.  

You talk to different AIs that carry different moods, attitudes, and languages. Each a reflection of how they express *tampo.*  It’s not about fixing what’s broken, but about finding the right tone to reach someone who still cares, but just won’t say it out loud.  


**What to expect:**

🤖 Personality-specific prompts and responses   
🎮 Explore different conversation paths  
💬 Receive mood-based responses from the AI  
🖌️ Mood-driven console art   
🛠️ Influence the AI’s mood and responses through choices   
🏆 Profiles and leaderboard   
📜 Lightweight history trimming for AI calls   
⚙️ Plain JDK, no external libraries required   

---

## 3) OOP Concepts Applied
- Abstraction
  - Lover (abstract class) defines the common interface/behavior (e.g., getPrompt()) for all personalities.
  - Ai encapsulates all AI HTTP call and parsing details behind chatWithAnalysis(List<Message>).
- Encapsulation
  - User, Message, and GameUtils expose minimal public methods; fields are scoped to keep state controlled.
  - Ai hides request construction, JSON parsing, and error handling details from Main.
- Inheritance
  - Concrete personalities (Tsundere, Kuudere, Deredere, Chuunibyou, Passionate, Timid, YoungStunna) extend Lover and override behavior/prompt.
- Polymorphism
  - Main holds a Lover reference and interacts uniformly, regardless of which concrete subclass is chosen.
  - Different Lover implementations customize responses via their prompts.
- Composition
  - Main composes a session from User + Lover + List<Message>.
  - ConsoleArt composes visual state from Emotion values returned by Ai.
- Enums and Strong Typing
  - Emotion, Gender, Language are enums that give compile-time safety and cleaner branching.

## Program Structure

```bash
T.A.M.P.O/
├─ .gitignore
├─ README.md
└─ src/
   ├─ display/
   │  └─ ConsoleArt.java
   ├─ lover/
   │  ├─ Chuunibyou.java
   │  ├─ Deredere.java
   │  ├─ Kuudere.java
   │  ├─ Passionate.java
   │  ├─ Timid.java
   │  ├─ Tsundere.java
   │  └─ YoungStunna.java
   ├─ main/
   │  ├─ Ai.java
   │  └─ Main.java
   ├─ savefile/
   │  └─ Info.txt
   └─ util/
      ├─ GameUtils.java
      ├─ Gender.java
      ├─ Language.java
      ├─ Lover.java
      ├─ Message.java
      └─ User.java
```

Main Roles:
- src.main.Main
  - Entry point. Menus (profiles, start game, leaderboard).
  - Orchestrates user input, builds history, calls Ai.chatWithAnalysis, updates score.
- src.main.Ai
  - Builds/sends HTTP requests to the Groq Chat Completions API.
  - Prepares message history (trimming), extracts content and mood JSON, returns ChatResponse.
- src.display.ConsoleArt (+ Emotion enum)
  - Renders mood-based ASCII art depending on the AI’s current emotion.
- src.util (helpers and models)
  - Lover (abstract base for personalities)
  - User (player profile)
  - Message (role + text container)
  - GameUtils (console utilities, save/load profiles and leaderboard)
  - Gender, Language (enums)
- src.lover (personalities)
  - Tsundere, Kuudere, Deredere, Chuunibyou, Passionate, Timid, YoungStunna (extend Lover)

Text-based relationships:
- Lover <|-- Tsundere | Kuudere | Deredere | Chuunibyou | Passionate | Timid | YoungStunna
- Main --> Lover, User, GameUtils, Ai
- Main <--> Ai.chatWithAnalysis(List<Message>)
- Ai --> ConsoleArt.Emotion (via ChatResponse.mood)
- Main --> ConsoleArt (displays mood)
- Main <--> GameUtils (profiles, leaderboard)
- Main <--> Message (builds conversation history)


---

## How to Run the Program (Windows, Command Line)
Prerequisites:
- JDK 21+ (tested with JDK 24)
- Internet connection (for AI replies)
- A Groq API key

Step-by-step:
1) Set your Groq API key in the source (no external config used):
   - Open src\main\Ai.java
   - Set:
     - private static final String API_KEY = "YOUR_GROQ_API_KEY";

2) Compile from the project root:
```powershell
# Create output folder
mkdir out 2>$null

# Compile all sources
javac -encoding UTF-8 -d out ^
  src\main\*.java ^
  src\util\*.java ^
  src\lover\*.java ^
  src\display\*.java
```

3) Run:
```powershell
java -cp out src.main.Main
```

Notes:
- If the API key is missing/invalid, AI features will not work.
- The program trims recent conversation history to reduce tokens.

---

## Sample Output

Lagay kayo screenshots dito

---

## Authors

<div align="center">

![Majol Abunis](static/majol.png "Majol Abunis")

  **CS 2102**  
**Developed by:**  
Alvendia, Marjol  
Borillo, Benedict  
Guial, Ron Emmanuel  

</div>

---

## Other Sections

### Future Enhancements
- Token-aware history trimming and retries on network errors
- Better validation for mood JSON
- Configurable personalities and dynamic difficulty

### References
- Groq Chat Completions API (OpenAI-compatible endpoint)
- Java 21+ standard library (java.net.http)
- Object-Oriented Design patterns (Abstraction, Encapsulation, Inheritance, Polymorphism)



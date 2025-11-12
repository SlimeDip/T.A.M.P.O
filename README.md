<div align="center">

# 🤖T.A.M.P.O. — The AI Mood-Patching Odyssey  
**An interactive console-based AI chat game that simulates emotional conversations through personality-driven responses and different personalities.**

*WIP*  
Game extremely buggy right now. still fixing bugs  

</div>

---

<div align="center">

  **CS 2102**  
**Developed by:**  
Alvendia, Marjol  
Borillo, Benedict  
Guial, Ron Emmanuel  

</div>

---

## 💭 Premise

In **T.A.M.P.O.**, words are your only tool.  
You talk to different AIs that carry different moods, attitudes, and languages. Each a reflection of how they express *tampo.*  
It’s not about fixing what’s broken, but about finding the right tone to reach someone who still cares, but just won’t say it out loud.  


**Users can:**

🤖 Interact with a personality-driven AI  
🎮 Explore different conversation paths  
💬 Receive mood-based responses from the AI  
🛠️ Influence the AI’s mood and responses through choices


---

## Project Structure

```bash
📂src/
├─ 📂lover/
│  ├─ Chuunibyou.java
│  ├─ Deredere.java
│  ├─ Hot.java
│  ├─ Kuudere.java
│  ├─ Lover.java
│  ├─ Tsundere.java
│  └─ YoungStunna.java
├─ 📂userinterface/
│  ├─ Gender.java
│  ├─ Language.java
│  ├─ Message.java
│  └─ User.java
├─ Ai.java
├─ GameUtils.java
└─ Main.java
```

---

## How to Run the Program  

1. **Open your terminal** in the `src/` folder of the project.  

2. **Compile all Java files**:
```bash
javac lover/*.java userinterface/*.java *.java
```
3. Run the program:
```bash
java Main
```

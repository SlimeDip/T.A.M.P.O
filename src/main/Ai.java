package src.main;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.List;
import src.util.Message;
import src.display.ConsoleArt.Emotion;

public class Ai {
    private static final String API_KEY = "";
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";

    private static String escapeJson(String s) {
        if (s == null) {
            return "";
        }

        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

  // To detect the emotion for the pixel art faces
public static Emotion detectEmotion(String response) {
    if (response == null || response.trim().isEmpty()) {
        return Emotion.NEUTRAL;
    }
    
    String lowerResponse = response.toLowerCase();
    
    // Check if "i love you" appears in a positive context, not in quotes or like sarcasm type of way
    if ((lowerResponse.contains("i love you") && 
         !lowerResponse.contains("\"i love you\"") &&
         !lowerResponse.contains("'i love you'") &&
         !lowerResponse.contains("simple i love you") &&
         !lowerResponse.contains("just i love you") &&
         !lowerResponse.contains("think i love you") &&
         !lowerResponse.contains("say i love you") &&
         !lowerResponse.matches(".*you think.*i love you.*") &&
         !lowerResponse.matches(".*a[n]? i love you.*")) ||
        lowerResponse.contains("i really love you") ||
        lowerResponse.contains("i truly love you")) {
        return Emotion.HAPPY;
    }
    //keywords in the responses of the AI that indicate different emotions
    if (lowerResponse.contains("disappointed") || 
        lowerResponse.contains("disappointing") ||
        lowerResponse.contains("distract") ||
        lowerResponse.contains("think") ||
        lowerResponse.contains("Save it") ||
        lowerResponse.contains("won't fix") ||
        lowerResponse.contains("let down")) {
        return Emotion.DISAPPOINTED;
    }
    
    if (lowerResponse.contains("hate") || 
        lowerResponse.contains("despise") ||
        lowerResponse.contains("angry") ||
        lowerResponse.contains("mad") ||
        lowerResponse.contains("stop") ||
        lowerResponse.contains("nothing") ||
        lowerResponse.contains("furious")) {
        return Emotion.ANGRY;
    }
    
    if (lowerResponse.contains("hmph") || 
        lowerResponse.contains("whatever") ||
        lowerResponse.contains("talking") ||
        lowerResponse.contains("not talking") ||
        lowerResponse.contains("late") ||
        lowerResponse.contains("cheap") ||
        lowerResponse.contains("prove") ||
        lowerResponse.contains("fine") ||
        lowerResponse.contains("leave me alone")) {
        return Math.random() > 0.5 ? Emotion.POUTING : Emotion.POUTING2;
    }
    
    // Default to neutral if there are no keywords detected
    return Emotion.NEUTRAL;
}

    // Core of the AI
    public String aiAnswer(List<Message> history) throws Exception {
        StringBuilder messagesJson = new StringBuilder();
        for (Message m : history) {
            messagesJson.append("{\"role\":\"")
                        .append(m.getRole()).append("\",\"content\":\"")
                        .append(escapeJson(m.getContent()))
                        .append("\"},");
        }

        if (messagesJson.length() > 0) {
            messagesJson.setLength(messagesJson.length() - 1);
        }

        String requestBody = String.format("""
            {
                "model": "llama-3.3-70b-versatile",
                "messages": [%s],
                "temperature": 0.7,
                "max_tokens": 1024
            }
            """, messagesJson.toString());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new Exception("API request failed with status: " + response.statusCode() + " - " + response.body());
        }
        
        return aiExtract(response.body());
    }

    // Para makuha yung content lang mula sa JSON
    private static String aiExtract(String json) {
        if (json == null) {
            return "";
        }

        String key = "\"content\"";
        int idx = json.indexOf(key);
        if (idx == -1) {
            return json;
        }
        
        int colon = json.indexOf(':', idx);
        if (colon == -1) {
            return json;
        }

        int startQuote = json.indexOf('"', colon);
        if (startQuote == -1) {
            return json;
        } 

        StringBuilder sb = new StringBuilder();
        boolean escape = false;
        for (int i = startQuote + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            if (escape) {
                if (c == 'n') {
                    sb.append('\n');
                } else if (c == 'r') {
                    sb.append('\r');
                } else if (c == 't') {
                    sb.append('\t');
                } else {
                    sb.append(c);
                }
                escape = false;
            } else {
                if (c == '\\') {
                    escape = true; 
                } else if (c == '"') {
                    break;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}
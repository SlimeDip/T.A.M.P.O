package src.main;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.List;
import src.display.ConsoleArt.Emotion;
import src.util.Message;

public class Ai {
    private static final String API_KEY = "";
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";

    public static class ChatResponse {
        public final String content;
        public final Emotion mood;
        public final boolean forgiven;

        public ChatResponse(String content, Emotion mood, boolean forgiven) {
            this.content = content;
            this.mood = mood;
            this.forgiven = forgiven;
        }
    }

    private static String escapeJsonString(String s) {
        if (s == null) {
            return "";
        }

        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    // Core of the AI (low-level HTTP request)
    private static String sendChatRequest(List<Message> history) throws Exception {
        StringBuilder messagesJson = new StringBuilder();
        for (Message m : history) {
            messagesJson.append("{\"role\":\"")
                        .append(m.getRole()).append("\",\"content\":\"")
                        .append(escapeJsonString(m.getContent()))
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
        
        return extractContent(response.body());
    }

    // AI response for mood, forgiveness, and content
    public static ChatResponse chatWithAnalysis(List<Message> history) throws Exception {
        List<Message> ans = new java.util.ArrayList<>(history);
        ans.add(new Message("user", """
            After you produce your normal assistant reply, on a new line ONLY output a JSON object
            exactly in this format with no extra text:
            {"mood":"HAPPY","forgiven":true}
            - mood must be one of: HAPPY, DISAPPOINTED, ANGRY, POUTING, NEUTRAL
            - forgiven must be true or false (boolean)
            Output the JSON on its own line after the reply.
            """));

        String full = sendChatRequest(ans).trim();

        int jsonIdx = full.indexOf("{\"mood\"");
        String content = full;
        String jsonPart = null;
        if (jsonIdx != -1) {
            content = full.substring(0, jsonIdx).trim();
            jsonPart = full.substring(jsonIdx).trim();
        }

        // default values
        Emotion mood = Emotion.NEUTRAL;
        boolean forgiven = false;

        // parse the JSON part
        if (jsonPart != null) {
            String up = jsonPart.replaceAll("\\s+", "");
            int mIdx = up.indexOf("\"mood\":\"");
            if (mIdx != -1) {
                int start = mIdx + 8;
                int end = up.indexOf("\"", start);
                if (end != -1) {
                    String moodStr = up.substring(start, end).toUpperCase();
                    switch (moodStr) {
                        case "HAPPY": mood = Emotion.HAPPY; break;
                        case "DISAPPOINTED": mood = Emotion.DISAPPOINTED; break;
                        case "ANGRY": mood = Emotion.ANGRY; break;
                        case "POUTING": mood = Emotion.POUTING; break;
                        default: mood = Emotion.NEUTRAL; break;
                    }
                }
            }

            int fIdx = up.indexOf("\"forgiven\":");
            if (fIdx != -1) {
                int start = fIdx + 11;
                int end = start;
                while (end < up.length() && (Character.isLetter(up.charAt(end)) || up.charAt(end) == 't' || up.charAt(end) == 'f')) end++;
                String val = up.substring(start, Math.min(end, up.length())).replaceAll("[,}]", "");
                forgiven = val.startsWith("true");
            }
        }

        return new ChatResponse(content, mood, forgiven);
    }

    // Para makuha yung content lang mula sa JSON
    private static String extractContent(String json) {
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
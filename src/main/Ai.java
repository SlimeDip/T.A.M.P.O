package src.main;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import src.display.ConsoleArt.Emotion;
import src.util.Message;

public class Ai {
    private static final String API_KEY = "";
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "llama-3.3-70b-versatile";
    private static final String ANALYSIS_PROMPT = """
            After you produce your normal assistant reply, on a new line ONLY output a JSON object exactly in this format with no extra text:
            {"mood":"HAPPY","forgiven":true}
            - mood must be one of: HAPPY, DISAPPOINTED, ANGRY, POUTING, NEUTRAL
            - forgiven must be true or false (boolean)
            Output the JSON on its own line after the reply.
            """;
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

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

    public static ChatResponse chatWithAnalysis(List<Message> history) throws Exception {
        List<Message> augmentedHistory = new ArrayList<>(history);
        augmentedHistory.add(new Message("system", ANALYSIS_PROMPT));

        String rawResponse = sendChatRequest(augmentedHistory).trim();

        // Debug: print the raw response
        //for (Message message : augmentedHistory) {
            //System.out.println(message.getRole() + ": " + message.getContent());
        //}
        // System.out.println("Raw AI Response: " + rawResponse);

        rawResponse = extractContent(rawResponse);
        return parseChatResponse(rawResponse);
    }

    private static String sendChatRequest(List<Message> history) throws Exception {
        String requestBody = String.format("""
                {
                    "model": "%s",
                    "messages": [%s],
                    "temperature": 0.7,
                    "max_tokens": 1024
                }
                """, MODEL, buildMessagesArray(history));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("API request failed with status: " + response.statusCode() + " - " + response.body());
        }

        return response.body();
    }

    private static ChatResponse parseChatResponse(String fullResponse) {
        int jsonIdx = fullResponse.indexOf("{\"mood\"");
        String content = jsonIdx == -1 ? fullResponse : fullResponse.substring(0, jsonIdx).trim();
        String normalizedJson = jsonIdx == -1 ? "" : fullResponse.substring(jsonIdx).replaceAll("\\s+", "");

        Emotion mood = extractMood(normalizedJson);
        boolean forgiven = extractForgiveness(normalizedJson);
        return new ChatResponse(content, mood, forgiven);
    }

    private static Emotion extractMood(String normalizedJson) {
        if (normalizedJson == null || normalizedJson.isEmpty()) {
            return Emotion.NEUTRAL;
        }
        int moodIdx = normalizedJson.indexOf("\"mood\":\"");
        if (moodIdx == -1) {
            return Emotion.NEUTRAL;
        }
        int start = moodIdx + 8;
        int end = normalizedJson.indexOf('"', start);
        if (end == -1) {
            return Emotion.NEUTRAL;
        }
        String token = normalizedJson.substring(start, end).toUpperCase();
        try {
            return Emotion.valueOf(token);
        } catch (IllegalArgumentException ex) {
            return Emotion.NEUTRAL;
        }
    }

    private static boolean extractForgiveness(String normalizedJson) {
        if (normalizedJson == null || normalizedJson.isEmpty()) {
            return false;
        }
        int forgivenIdx = normalizedJson.indexOf("\"forgiven\":");
        if (forgivenIdx == -1) {
            return false;
        }
        int start = forgivenIdx + 11;
        if (start >= normalizedJson.length()) {
            return false;
        }
        if (normalizedJson.startsWith("true", start)) {
            return true;
        }
        if (normalizedJson.startsWith("false", start)) {
            return false;
        }
        return false;
    }

    private static String buildMessagesArray(List<Message> history) {
        if (history == null || history.isEmpty()) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(",");
        for (Message message : history) {
            String role = message.getRole();
            String content = escapeJsonString(message.getContent());
            joiner.add(String.format("{\"role\":\"%s\",\"content\":\"%s\"}", role, content));
        }
        return joiner.toString();
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
        reading:
        for (int i = startQuote + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            if (escape) {
                switch (c) {
                    case 'n' -> sb.append('\n');
                    case 'r' -> sb.append('\r');
                    case 't' -> sb.append('\t');
                    default -> sb.append(c);
                }
                escape = false;
            } else {
                switch (c) {
                    case '\\' -> escape = true;
                    case '"' -> { break reading; }
                    default -> sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}
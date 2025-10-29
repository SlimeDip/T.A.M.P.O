import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.List;

public class Ai {
    // Pls lang libre lang yung API key, baka naman nakawin nyo pa
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";

    private static String escapeJson(String s) {
        if (s == null) {
            return "";
        }

        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    // Core of the AI
    public String aiAnswer(List<Message> history) throws Exception {
        StringBuilder messagesJson = new StringBuilder();
        for (Message m : history) {
            messagesJson.append("{\"role\":\"").append(m.role).append("\",\"content\":\"").append(escapeJson(m.content)).append("\"},");
        }

        if (messagesJson.length() > 0) {
            messagesJson.setLength(messagesJson.length() - 1);
        }

        String requestBody = String.format("""
            {
                "model": "llama-3.1-8b-instant",
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

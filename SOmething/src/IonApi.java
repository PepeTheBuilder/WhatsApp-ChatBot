import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.json.JSONObject;


public class IonApi {
    public static Map<String, Object> ionNewSession() throws IOException {
        String url = Your_bot_api_url_for_creation_of_new_sesion;
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        setHeaders(connection, headers);

        int statusCode = connection.getResponseCode();
        String jsonResponse = readResponse(connection);

        Map<String, Object> result = new HashMap<>();
        result.put("statusCode", statusCode);
        result.put("response", jsonResponse);

        return result;
    }

    public static Map<String, Object> ionSentQuestion(String sessionId, String message)
            throws IOException {
        String url =  Your_bot_api_url_for_response;
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("Content-Type", "application/json");

        Map<String, Object> data = new HashMap<>();
        data.put("sessionId", sessionId);
        data.put("service", Your_type_of_service);
        data.put("message", message);
        data.put("language", "ro-RO");

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        setHeaders(connection, headers);
        writeData(connection, data);

        int statusCode = connection.getResponseCode();
        String jsonResponse = "";
        if (statusCode == 200) {
            jsonResponse = readResponse(connection);
        } else {
            jsonResponse = readError(connection);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("statusCode", statusCode);
        result.put("response", jsonResponse);

        return result;
    }

    private static String readError(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(connection.getErrorStream())) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }
        return response.toString();
    }

    private static void setHeaders(HttpURLConnection connection, Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private static void writeData(HttpURLConnection connection, Map<String, Object> data) throws IOException {
        connection.setDoOutput(true);
        JSONObject jsonObject = new JSONObject(data);
        connection.getOutputStream().write(jsonObject.toString().getBytes());
    }

    private static String readResponse(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }
        return response.toString();
    }

    public static String runPowerShellScript(String text) {
        try {
            // Define the PowerShell script
            String script = "$headers = @{'Authorization'='Bearer "+Your_WhatsApp_Token+"';'Content-Type'='application/json'};$body=@{messaging_product='whatsapp';to='"+PhoneNumber+"';type='template';template=@{name='hello_world';language=@{code='en_US'}}}|ConvertTo-Json;Invoke-RestMethod -Uri 'https://graph.facebook.com/v17.0/128378787036385/messages' -Method Post -Headers $headers -Body $body";

            // Define the command to run the PowerShell script
            String[] command = {"powershell.exe", "-Command", script};

            // Create a ProcessBuilder
            ProcessBuilder pb = new ProcessBuilder(command);

            // Start the process
            Process process = pb.start();

            // Read the output
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // Check for any errors
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}
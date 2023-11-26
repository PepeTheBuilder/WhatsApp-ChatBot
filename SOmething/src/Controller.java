import org.json.JSONObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;


public class Controller {
    private Interfata view;
    private String sessionId; // Variable to store the session ID

    public Controller(Interfata view) {
        this.view = view;
        this.view.setVisible(true);
        this.view.addButtonListener(new Button1Listener());

        try {
            // Perform the new session request when the Controller is instantiated
            Map<String, Object> newSessionResult = IonApi.ionNewSession();
            int statusCode = (int) newSessionResult.get("statusCode");
            String response = (String) newSessionResult.get("response");

            if (statusCode == 200) {
                // Assuming the response contains the session ID
                this.sessionId = removeExecessText(response); // Extract the session ID from the response
                System.out.println("New session created. Session ID: " + statusCode + ", Response: " + sessionId );
            } else {
                // Handle the error case accordingly
                System.out.println("Error creating a new session. Status Code: " + statusCode + ", Response: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String removeExecessText(String text) {
        String result = text.replaceFirst("\\{\"sessionId\":\"","");
        result= result.replaceAll("\"\\}", "");
        return result;
    }
    public String extractCompletion(String response) {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getJSONObject("response").getString("completion");
    }


    class Button1Listener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            // Get the question from the text area
            String question = view.getTextArea1().getText();

            try {
                // Perform the question submission
                Map<String, Object> sentQuestionResult = IonApi.ionSentQuestion(sessionId, question);
                int statusCode = (int) sentQuestionResult.get("statusCode");
                String response = (String) sentQuestionResult.get("response");
                System.out.println(response);
                response = extractCompletion(response);
                try {
                    String scriptResponse = IonApi.runPowerShellScript(response);
                    // Utilizează răspunsul scriptului în loc să-l setezi direct în textarea2
                    view.setTextArea2(scriptResponse);
                } catch (Exception e) {
                    System.out.println("Error sending message " + e.getMessage());
                }

                if (statusCode != 200) {
                    // Handle the error case accordingly
                    System.out.println("Error submitting question. Status Code: " + statusCode + ", Response: " + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
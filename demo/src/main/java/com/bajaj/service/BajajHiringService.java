package com.bajaj.service;

import com.bajaj.model.WebhookRequest;
import com.bajaj.model.WebhookResponse;
import com.bajaj.model.SolutionPayload;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BajajHiringService {

    private final RestTemplate restTemplate;

    public BajajHiringService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void executeHiringTest(String name, String regNo, String email) {
        try {
            System.out.println("🚀 Starting Bajaj Hiring Test...");

            // Step 1: Generate Webhook
            System.out.println("\n📤 Step 1: Generating webhook...");
            WebhookResponse webhookResponse = generateWebhook(name, regNo, email);

            if (webhookResponse == null || webhookResponse.getWebhook() == null) {
                System.err.println("❌ Failed to generate webhook");
                return;
            }

            String webhookUrl = webhookResponse.getWebhook();
            String accessToken = webhookResponse.getAccessToken();

            System.out.println("✅ Webhook Generated!");
            System.out.println("🔗 Webhook URL: " + webhookUrl);

            // Step 2: Solve SQL Problem
            System.out.println("\n📝 Step 2: Solving SQL Problem...");
            String sqlQuery = solveSQLProblem(regNo);

            // Step 3: Submit Solution
            System.out.println("\n📤 Step 3: Submitting solution...");
            submitSolution(webhookUrl, accessToken, sqlQuery);
            System.out.println("✅ Solution submitted successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private WebhookResponse generateWebhook(String name, String regNo, String email) {
        try {
            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            WebhookRequest request = new WebhookRequest(name, regNo, email);
            HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(
                    url,
                    entity,
                    WebhookResponse.class
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error generating webhook: " + e.getMessage());
            return null;
        }
    }

    private String solveSQLProblem(String regNo) {
        String digits = regNo.replaceAll("\\D+", "");
        int lastTwoDigits = Integer.parseInt(digits.substring(digits.length() - 2));

        if (lastTwoDigits % 2 == 0) {
            // Question 2 (Even) - Your case
            return "SELECT d.DEPARTMENT_NAME, " +
                    "ROUND(AVG(DATEDIFF(YEAR, e.DOB, GETDATE())), 2) AS AVERAGE_AGE, " +
                    "STRING_AGG(CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME), ', ') WITHIN GROUP (ORDER BY e.EMP_ID) AS EMPLOYEE_LIST " +
                    "FROM DEPARTMENT d " +
                    "LEFT JOIN EMPLOYEE e ON d.DEPARTMENT_ID = e.DEPARTMENT " +
                    "LEFT JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID " +
                    "WHERE p.AMOUNT > 70000 " +
                    "GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME " +
                    "ORDER BY d.DEPARTMENT_ID DESC;";
        } else {
            return "SELECT * FROM YOUR_TABLE WHERE CONDITION;";
        }
    }

    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);

            SolutionPayload payload = new SolutionPayload(sqlQuery);
            HttpEntity<SolutionPayload> entity = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    webhookUrl,
                    entity,
                    String.class
            );

            System.out.println("✅ Response: " + response.getStatusCode());
        } catch (Exception e) {
            System.err.println("Error submitting solution: " + e.getMessage());
        }
    }
}

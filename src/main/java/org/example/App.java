package org.example;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class App
{
    public static void main( String[] args )
    {
        // Add link to the mp4
        final String recordingLink = "https://github.com/stanchishe/Oct2024S2T/blob/master/external_r/Thirsty%20Pretzels.mp4?raw=true";
        // add Transcript API endpoint
        final String transcriptLink = "https://api.assemblyai.com/v2/transcript";

        URI transcriptUri;

        try {
            transcriptUri = new URI(transcriptLink);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Create the initial Req. body object
        Transcript transcriptBody = new Transcript();
        transcriptBody.setAudio_url(recordingLink);

        Gson gson = new Gson();

        // Convert transcriptObj to Req body:
        String jsonReqBody = gson.toJson(transcriptBody);

        HttpRequest postRequest = HttpRequest.newBuilder()
                // Add URI
                .uri(transcriptUri)
                // Add header values
                .header("Authorization", "8991f694ac1644e2bbe783d8350568f1")
                // Add HTTP method and Req body
                .POST(HttpRequest.BodyPublishers.ofString(jsonReqBody))
                // Build the request
                .build();

        // We built the Request, now let's send it!
        HttpClient httpClient = HttpClient.newHttpClient();

        // Also, we need to receive the result:
        HttpResponse<String> postResponse;

        try {
            postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Show the raw result:
        //System.out.println(postResponse.body());

        // Deserialize the body into a Java class
        Transcript postResultOjb = gson.fromJson(postResponse.body(), Transcript.class);

        // Show some specific value:
        System.out.println("The id of the uploaded file is : " + postResultOjb.getId());
        System.out.println("The status of the uploaded file is : " + postResultOjb.getStatus());

        // Create the second Request

        // Create a new URI obj
        URI getUri;

        try {
            getUri = new URI(transcriptLink + "/" + postResultOjb.getId());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Build tge GET Req
        HttpRequest getRequest = HttpRequest.newBuilder()
                // Add uri
                .uri(getUri)
                // Add header
                .header("Authorization", "8991f694ac1644e2bbe783d8350568f1")
                // build
                .build();

        HttpResponse<String> getResponse;
        Transcript getResponseOjb;
        while (true) {
            try {
                getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Convert the body JSON to a Java class
            getResponseOjb = gson.fromJson(getResponse.body(), Transcript.class);

            // Print the status of the transcript:
            System.out.println("The status if the transcript request is: " + getResponseOjb.getStatus());

            // Add a check to verify if the correct status is reached:
            if(
                    "completed".equalsIgnoreCase(getResponseOjb.getStatus())
                            || "error".equalsIgnoreCase(getResponseOjb.getStatus())
            ) {
                break;
            }

            // Sleep!
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if("completed".equalsIgnoreCase(getResponseOjb.getStatus())) {
            System.out.println("Transcript was generated Successfully!");
            System.out.println("The text is: " + getResponseOjb.getText());
            System.out.println("The language is is: "+ getResponseOjb.getLanguage_code());
            System.out.println("The length is: " + getResponseOjb.getAudio_duration());
        } else {
            System.out.println("The generation for the transcript FAILED! VERY SAD!");
        }
    }
}

package org.metadatacenter.nih.ingestor.poster;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.nih.ingestor.exceptions.RESTRequestFailedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Poster {
    private String apiKey;
    private String folderId;
    private URL urlForValidateRequest;
    private URL urlForPut;
    ObjectMapper mapper = new ObjectMapper();
    final ObjectWriter writer = mapper.writer();

    public Poster(String apiKey, String folderId) throws IOException {
        this.apiKey = apiKey;
        this.folderId = folderId;
        this.urlForValidateRequest = new URL(RequestURLs.validateURL);
        this.urlForPut = new URL(RequestURLs.putURL + folderId);
    }

    private HttpURLConnection createAndOpenConnection(URL urlForRequest) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) urlForRequest.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "apiKey " + apiKey);
        return connection;
    }

    public void validate(ObjectNode cde) throws IOException, RESTRequestFailedException {
        HttpURLConnection connection = createAndOpenConnection(urlForValidateRequest);
        OutputStream os = connection.getOutputStream();
        byte[] cdeBytes = writer.writeValueAsBytes(cde);
        os.write(cdeBytes);
        os.flush();
        os.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println(getResponseBody(connection));
            connection.disconnect();
        } else {
            throw new RESTRequestFailedException("POST", responseCode);
        }
    }

    public void put(ObjectNode cde) throws IOException, RESTRequestFailedException {
        HttpURLConnection connection = createAndOpenConnection(urlForPut);
        OutputStream os = connection.getOutputStream();
        byte[] cdeBytes = writer.writeValueAsBytes(cde);
        os.write(cdeBytes);
        os.flush();
        os.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            connection.disconnect();
        } else {
            throw new RESTRequestFailedException("POST", responseCode);
        }
    }

    private String getResponseBody(HttpURLConnection connection) throws IOException {
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        while ((line = br.readLine()) != null)
            response.append(line);
        return response.toString();
    }
}

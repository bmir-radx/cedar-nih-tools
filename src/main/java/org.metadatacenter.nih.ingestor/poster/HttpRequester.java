package org.metadatacenter.nih.ingestor.poster;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.nih.ingestor.constants.CEDARJsonKeys;
import org.metadatacenter.nih.ingestor.constants.CEDARResourceTypes;
import org.metadatacenter.nih.ingestor.constants.NIHJsonKeys;
import org.metadatacenter.nih.ingestor.constants.RESTJsonKeys;
import org.metadatacenter.nih.ingestor.exceptions.RESTRequestFailedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpRequester {
    private final String apiKey;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter writer = mapper.writer();

    public HttpRequester(String apiKey) {
        this.apiKey = apiKey;
    }

    protected HttpURLConnection createAndOpenConnection(URL urlForRequest, String requestMethod) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) urlForRequest.openConnection();
        connection.setRequestMethod(requestMethod);
        if (requestMethod.equals(HttpRequestConstants.POST)) {
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
        }
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "apiKey " + apiKey);
        return connection;
    }

    public void validate(ObjectNode cde) throws IOException, RESTRequestFailedException {
        URL urlForValidateRequest = new URL(RequestURLPrefixes.validateURL);
        HttpURLConnection connection = createAndOpenConnection(urlForValidateRequest, HttpRequestConstants.POST);
        OutputStream os = connection.getOutputStream();
        byte[] cdeBytes = writer.writeValueAsBytes(cde);
        os.write(cdeBytes);
        os.flush();
        os.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // System.out.println(getResponseBody(connection.getInputStream()));
            connection.disconnect();
        } else {
            throw new RESTRequestFailedException(HttpRequestConstants.POST, responseCode);
        }
    }

    public void deleteArtifacts(String artifactURLPrefix, List<String> artifactIds) throws IOException, RESTRequestFailedException {
        for (String artifactId: artifactIds) {
            deleteSingleArtifact(artifactURLPrefix, artifactId);
        }
    }

    public void deleteSingleArtifact(String artifactURLPrefix, String artifactId) throws IOException, RESTRequestFailedException {
        String encodedItemId = URLEncoder.encode(artifactId, StandardCharsets.UTF_8.toString());
        URL urlForDelete = new URL(artifactURLPrefix + encodedItemId);
        System.out.println("Deleting: " + artifactId);
        HttpURLConnection connection = createAndOpenConnection(urlForDelete, HttpRequestConstants.DELETE);
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
            connection.disconnect();
            System.out.println("Deleted: " + artifactId);
        } else {
            throw new RESTRequestFailedException(HttpRequestConstants.DELETE, responseCode);
        }
    }

    public void publishArtifacts(List<String> artifactIds) throws IOException, RESTRequestFailedException {
        for (String artifactId: artifactIds) {
            publishSingleArtifact(artifactId);
        }
    }

    /*
    Default to publishing as 1.0.0. This should be reviewed.
     */
    public void publishSingleArtifact(String artifactId) throws IOException, RESTRequestFailedException {
        HttpURLConnection connection = createAndOpenConnection(
                new URL(RequestURLPrefixes.publishURL), HttpRequestConstants.POST);
        System.out.println("Publishing: " + artifactId);
        String requestBody = String.format("{\"@id\": \"%s\", \"newVersion\": \"1.0.0\"}", artifactId);
        JsonNode node = mapper.readTree(requestBody);
        System.out.println(node);
        OutputStream os = connection.getOutputStream();
        byte[] requestBytes = writer.writeValueAsBytes(node);
        os.write(requestBytes);
        os.flush();
        os.close();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            connection.disconnect();
            System.out.println("Published: " + artifactId);
        } else {
            throw new RESTRequestFailedException(HttpRequestConstants.POST, responseCode);
        }
    }

    /*
    Successful search returns a HTTP_OK.
     */
    public ArrayList<String> findFolders(String folderId) throws IOException, RESTRequestFailedException {
        ArrayList<String> folders = new ArrayList<>();
        folders.add(folderId);
        // requires encoding the folderId
        String encodedFolderId = URLEncoder.encode(folderId, StandardCharsets.UTF_8.toString());

        // search folders recursively and search until pages are exhausted
        int offset = 0;
        int numItemsInLastPage = 0;
        while (offset == 0 || numItemsInLastPage != 0) {
            URL urlForFolderSearch = new URL(RequestURLPrefixes.createSearchURL(encodedFolderId, CEDARResourceTypes.FOLDER, offset));
            HttpURLConnection connection = createAndOpenConnection(urlForFolderSearch, HttpRequestConstants.GET);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = getResponseBody(connection.getInputStream());
                JsonNode resources = mapper.readTree(response).get(RESTJsonKeys.RESOURCES);
                numItemsInLastPage = resources.size();
                offset += RequestURLPrefixes.SEARCHPAGELIMIT;
                for (JsonNode node : resources) {
                    String childFolderId = node.get(CEDARJsonKeys.ID).toString();
                    String trimmedChildFolderId = childFolderId.substring(1, childFolderId.length() - 1);
                    folders.addAll(findFolders(trimmedChildFolderId));
                }
                connection.disconnect();
            } else {
                throw new RESTRequestFailedException(HttpRequestConstants.GET, responseCode);
            }
        }
        return folders;
    }

    public ArrayList<String> findFields(ArrayList<String> folderIds, boolean ignorePublished) throws IOException, RESTRequestFailedException {
        ArrayList<String> fields = new ArrayList<>();
        for (String folderId : folderIds) {
            fields.addAll(findFieldsInSingleFolder(folderId, ignorePublished));
        }
        return fields;
    }

    public ArrayList<String> findFieldsInSingleFolder(String folderId, boolean ignorePublished) throws IOException, RESTRequestFailedException {
        ArrayList<String> fields = new ArrayList<>();
        // search folder until pages are exhausted
        int offset = 0;
        int numItemsInLastPage = 0;
        while (offset == 0 || numItemsInLastPage != 0) {
            String encodedFolderId = URLEncoder.encode(folderId, StandardCharsets.UTF_8.toString());
            URL urlForFieldSearch = new URL(RequestURLPrefixes.createSearchURL(encodedFolderId, CEDARResourceTypes.FIELD, offset));
            HttpURLConnection connection = createAndOpenConnection(urlForFieldSearch, HttpRequestConstants.GET);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = getResponseBody(connection.getInputStream());
                JsonNode resources = mapper.readTree(response).get(RESTJsonKeys.RESOURCES);
                numItemsInLastPage = resources.size();
                offset += RequestURLPrefixes.SEARCHPAGELIMIT;
                for (JsonNode node : resources) {
                    String fieldId = node.get(CEDARJsonKeys.ID).toString();
                    if (ignorePublished) {
                        String status = node.get(CEDARJsonKeys.STATUS).toString();
                        status = status.substring(1, status.length() - 1);
                        if (!status.equals(CEDARJsonKeys.PUBLISHED)) {
                            fields.add(fieldId.substring(1, fieldId.length() - 1));
                        }
                    }
                }
                connection.disconnect();
            } else {
                throw new RESTRequestFailedException(HttpRequestConstants.GET, responseCode);
            }
        }
        return fields;
    }

    public void uploadDraft(ObjectNode cde, String folderId) throws IOException, RESTRequestFailedException {
        URL urlForPut = new URL(RequestURLPrefixes.putURL + folderId);
        HttpURLConnection connection = createAndOpenConnection(urlForPut, HttpRequestConstants.POST);
        OutputStream os = connection.getOutputStream();
        byte[] cdeBytes = writer.writeValueAsBytes(cde);
        os.write(cdeBytes);
        os.flush();
        os.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            connection.disconnect();
        } else {
            throw new RESTRequestFailedException(HttpRequestConstants.POST, responseCode);
        }
    }

    private String getResponseBody(InputStream inputStream) throws IOException {
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        while ((line = br.readLine()) != null)
            response.append(line);
        return response.toString();
    }
}

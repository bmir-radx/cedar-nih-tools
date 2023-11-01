package org.metadatacenter.nih.ingestor.poster;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.metadatacenter.nih.ingestor.exceptions.RESTRequestFailedException;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class PosterTest {

    String apiKey = "dummyApiKey";
    String folderId = "dummyFolderId";
    ObjectMapper mapper = new ObjectMapper();
    OutputStream mockOutputStream;
    HttpURLConnection mockConnection;
    Poster spyPoster;

    @Before
    public void setUp() throws IOException {
        mockOutputStream = Mockito.mock(OutputStream.class);
        Mockito.doNothing().when(mockOutputStream).write(Mockito.any());
        Mockito.doNothing().when(mockOutputStream).flush();
        Mockito.doNothing().when(mockOutputStream).close();

        mockConnection = Mockito.mock(HttpURLConnection.class);
        Mockito.doReturn(mockOutputStream).when(mockConnection).getOutputStream();
        Mockito.doNothing().when(mockConnection).disconnect();

        spyPoster = Mockito.spy(new Poster(apiKey, folderId));
        Mockito.doReturn(mockConnection).when(spyPoster).createAndOpenConnection(Mockito.any());
    }

    @Test(expected = RESTRequestFailedException.class)
    public void testValidateFailure() throws IOException, RESTRequestFailedException {
        // simulate response to a bad request
        Mockito.doReturn(HttpURLConnection.HTTP_BAD_REQUEST).when(mockConnection).getResponseCode();

        ObjectNode node = mapper.createObjectNode();
        spyPoster.validate(node);

        Mockito.verify(mockConnection, Mockito.times(1)).getOutputStream();
        Mockito.verify(mockConnection, Mockito.times(1)).getResponseCode();
        Mockito.verify(mockConnection, Mockito.times(0)).disconnect();
    }

    @Test
    public void testValidateSuccess() throws IOException, RESTRequestFailedException {
        // simulate a successful request
        Mockito.doReturn(HttpURLConnection.HTTP_OK).when(mockConnection).getResponseCode();

        ObjectNode node = mapper.createObjectNode();
        spyPoster.validate(node);

        Mockito.verify(mockConnection, Mockito.times(1)).getOutputStream();
        Mockito.verify(mockConnection, Mockito.times(1)).getResponseCode();
        Mockito.verify(mockConnection, Mockito.times(1)).disconnect();
    }

    @Test(expected = RESTRequestFailedException.class)
    public void testPutFailure() throws IOException, RESTRequestFailedException {
        // simulate response to a bad request
        Mockito.doReturn(HttpURLConnection.HTTP_BAD_REQUEST).when(mockConnection).getResponseCode();

        ObjectNode node = mapper.createObjectNode();
        spyPoster.put(node);

        Mockito.verify(mockConnection, Mockito.times(1)).getOutputStream();
        Mockito.verify(mockConnection, Mockito.times(1)).getResponseCode();
        Mockito.verify(mockConnection, Mockito.times(0)).disconnect();
    }

    @Test
    public void testPutSuccess() throws IOException, RESTRequestFailedException {
        // simulate response to a successful PUT
        Mockito.doReturn(HttpURLConnection.HTTP_CREATED).when(mockConnection).getResponseCode();

        ObjectNode node = mapper.createObjectNode();
        spyPoster.put(node);

        Mockito.verify(mockConnection, Mockito.times(1)).getOutputStream();
        Mockito.verify(mockConnection, Mockito.times(1)).getResponseCode();
        Mockito.verify(mockConnection, Mockito.times(1)).disconnect();
    }
}


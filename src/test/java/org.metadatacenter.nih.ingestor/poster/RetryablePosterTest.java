package org.metadatacenter.nih.ingestor.poster;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.ConnectException;
import java.net.SocketException;

public class RetryablePosterTest {
    ObjectMapper mapper = new ObjectMapper();
    Integer numMaxAttempts = 3;
    String folderId = "dummyFolderId";

    @Test
    public void testValidate_success() throws Throwable {
        Poster mockPoster = Mockito.mock(Poster.class);
        Mockito.doNothing().when(mockPoster).validate(Mockito.any());
        RetryablePoster retryablePoster = new RetryablePoster(mockPoster, "validate");

        ObjectNode node = mapper.createObjectNode();
        retryablePoster.validate(node);

        // succeed on first try
        Mockito.verify(mockPoster, Mockito.times(1)).validate(node);
    }

    @Test
    public void testValidate_failureThenSuccess() throws Throwable {
        Poster mockPoster = Mockito.mock(Poster.class);
        Mockito.doThrow(ConnectException.class)
                .doThrow(SocketException.class)
                .doNothing()
                .when(mockPoster).validate(Mockito.any());
        RetryablePoster retryablePoster = new RetryablePoster(mockPoster, "validate");

        ObjectNode node = mapper.createObjectNode();
        retryablePoster.validate(node);

        // fail twice and then succeed
        Mockito.verify(mockPoster, Mockito.times(3)).validate(node);
    }

    @Test(expected = ConnectException.class)
    public void testValidate_failure() throws Throwable {
        Poster mockPoster = Mockito.mock(Poster.class);
        Mockito.doThrow(ConnectException.class).when(mockPoster).validate(Mockito.any());
        RetryablePoster retryablePoster = new RetryablePoster(mockPoster, "validate");

        ObjectNode node = mapper.createObjectNode();
        retryablePoster.validate(node);

        // fail until out of attempts
        Mockito.verify(mockPoster, Mockito.times(numMaxAttempts)).validate(node);
    }

    @Test
    public void testPut_success() throws Throwable {
        Poster mockPoster = Mockito.mock(Poster.class);
        Mockito.doNothing().when(mockPoster).put(Mockito.any(), Mockito.eq(folderId));
        RetryablePoster retryablePoster = new RetryablePoster(mockPoster, "put");

        ObjectNode node = mapper.createObjectNode();
        retryablePoster.put(node, folderId);

        // succeed on first try
        Mockito.verify(mockPoster, Mockito.times(1)).put(node, folderId);
    }

    @Test
    public void testPut_failureThenSuccess() throws Throwable {
        Poster mockPoster = Mockito.mock(Poster.class);
        Mockito.doThrow(ConnectException.class)
                .doThrow(SocketException.class)
                .doNothing()
                .when(mockPoster).put(Mockito.any(), Mockito.eq(folderId));
        RetryablePoster retryablePoster = new RetryablePoster(mockPoster, "put");

        ObjectNode node = mapper.createObjectNode();
        retryablePoster.put(node, folderId);

        // fail twice and then succeed
        Mockito.verify(mockPoster, Mockito.times(3)).put(node, folderId);
    }

    @Test(expected = ConnectException.class)
    public void testPut_failure() throws Throwable {
        Poster mockPoster = Mockito.mock(Poster.class);
        Mockito.doThrow(ConnectException.class).when(mockPoster).put(Mockito.any(), Mockito.eq(folderId));
        RetryablePoster retryablePoster = new RetryablePoster(mockPoster, "put");

        ObjectNode node = mapper.createObjectNode();
        retryablePoster.put(node, folderId);

        // fail until out of attempts
        Mockito.verify(mockPoster, Mockito.times(numMaxAttempts)).put(node, folderId);
    }
}

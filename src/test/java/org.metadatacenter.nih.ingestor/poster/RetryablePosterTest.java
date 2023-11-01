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

    @Test
    public void testValidateSuccess() throws Throwable {
        Poster mockPoster = Mockito.mock(Poster.class);
        Mockito.doNothing().when(mockPoster).validate(Mockito.any());
        RetryablePoster retryablePoster = new RetryablePoster(mockPoster, "validate");

        ObjectNode node = mapper.createObjectNode();
        retryablePoster.validate(node);

        // succeed on first try
        Mockito.verify(mockPoster, Mockito.times(1)).validate(node);
    }

    @Test
    public void testValidateFailureThenSuccess() throws Throwable {
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
    public void testValidateFailure() throws Throwable {
        Poster mockPoster = Mockito.mock(Poster.class);
        Mockito.doThrow(ConnectException.class).when(mockPoster).validate(Mockito.any());
        RetryablePoster retryablePoster = new RetryablePoster(mockPoster, "validate");

        ObjectNode node = mapper.createObjectNode();
        retryablePoster.validate(node);

        // fail until out of attempts
        Mockito.verify(mockPoster, Mockito.times(numMaxAttempts)).validate(node);
    }

    @Test
    public void testPutSuccess() throws Throwable {
        Poster mockPoster = Mockito.mock(Poster.class);
        Mockito.doNothing().when(mockPoster).put(Mockito.any());
        RetryablePoster retryablePoster = new RetryablePoster(mockPoster, "put");

        ObjectNode node = mapper.createObjectNode();
        retryablePoster.put(node);

        // succeed on first try
        Mockito.verify(mockPoster, Mockito.times(1)).put(node);
    }

    @Test
    public void testPutFailureThenSuccess() throws Throwable {
        Poster mockPoster = Mockito.mock(Poster.class);
        Mockito.doThrow(ConnectException.class)
                .doThrow(SocketException.class)
                .doNothing()
                .when(mockPoster).put(Mockito.any());
        RetryablePoster retryablePoster = new RetryablePoster(mockPoster, "put");

        ObjectNode node = mapper.createObjectNode();
        retryablePoster.put(node);

        // fail twice and then succeed
        Mockito.verify(mockPoster, Mockito.times(3)).put(node);
    }

    @Test(expected = ConnectException.class)
    public void testPutFailure() throws Throwable {
        Poster mockPoster = Mockito.mock(Poster.class);
        Mockito.doThrow(ConnectException.class).when(mockPoster).put(Mockito.any());
        RetryablePoster retryablePoster = new RetryablePoster(mockPoster, "put");

        ObjectNode node = mapper.createObjectNode();
        retryablePoster.put(node);

        // fail until out of attempts
        Mockito.verify(mockPoster, Mockito.times(numMaxAttempts)).put(node);
    }
}
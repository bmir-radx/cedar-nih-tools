package org.metadatacenter.nih.ingestor.poster;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.ConnectException;
import java.net.SocketException;

public class RetryableHttpRequesterTest {
    ObjectMapper mapper = new ObjectMapper();
    Integer numMaxAttempts = 3;
    String folderId = "dummyFolderId";

    @Test
    public void testValidate_success() throws Throwable {
        HttpRequester mockHttpRequester = Mockito.mock(HttpRequester.class);
        Mockito.doNothing().when(mockHttpRequester).validate(Mockito.any());
        RetryableRequester retryableRequester = new RetryableRequester(mockHttpRequester, "validate");

        ObjectNode node = mapper.createObjectNode();
        retryableRequester.validate(node);

        // succeed on first try
        Mockito.verify(mockHttpRequester, Mockito.times(1)).validate(node);
    }

    @Test
    public void testValidate_failureThenSuccess() throws Throwable {
        HttpRequester mockHttpRequester = Mockito.mock(HttpRequester.class);
        Mockito.doThrow(ConnectException.class)
                .doThrow(SocketException.class)
                .doNothing()
                .when(mockHttpRequester).validate(Mockito.any());
        RetryableRequester retryableRequester = new RetryableRequester(mockHttpRequester, "validate");

        ObjectNode node = mapper.createObjectNode();
        retryableRequester.validate(node);

        // fail twice and then succeed
        Mockito.verify(mockHttpRequester, Mockito.times(3)).validate(node);
    }

    @Test(expected = ConnectException.class)
    public void testValidate_failure() throws Throwable {
        HttpRequester mockHttpRequester = Mockito.mock(HttpRequester.class);
        Mockito.doThrow(ConnectException.class).when(mockHttpRequester).validate(Mockito.any());
        RetryableRequester retryableRequester = new RetryableRequester(mockHttpRequester, "validate");

        ObjectNode node = mapper.createObjectNode();
        retryableRequester.validate(node);

        // fail until out of attempts
        Mockito.verify(mockHttpRequester, Mockito.times(numMaxAttempts)).validate(node);
    }

    @Test
    public void testPut_success() throws Throwable {
        HttpRequester mockHttpRequester = Mockito.mock(HttpRequester.class);
        Mockito.doNothing().when(mockHttpRequester).uploadDraft(Mockito.any(), Mockito.eq(folderId));
        RetryableRequester retryableRequester = new RetryableRequester(mockHttpRequester, "put");

        ObjectNode node = mapper.createObjectNode();
        retryableRequester.put(node, folderId);

        // succeed on first try
        Mockito.verify(mockHttpRequester, Mockito.times(1)).uploadDraft(node, folderId);
    }

    @Test
    public void testPut_failureThenSuccess() throws Throwable {
        HttpRequester mockHttpRequester = Mockito.mock(HttpRequester.class);
        Mockito.doThrow(ConnectException.class)
                .doThrow(SocketException.class)
                .doNothing()
                .when(mockHttpRequester).uploadDraft(Mockito.any(), Mockito.eq(folderId));
        RetryableRequester retryableRequester = new RetryableRequester(mockHttpRequester, "put");

        ObjectNode node = mapper.createObjectNode();
        retryableRequester.put(node, folderId);

        // fail twice and then succeed
        Mockito.verify(mockHttpRequester, Mockito.times(3)).uploadDraft(node, folderId);
    }

    @Test(expected = ConnectException.class)
    public void testPut_failure() throws Throwable {
        HttpRequester mockHttpRequester = Mockito.mock(HttpRequester.class);
        Mockito.doThrow(ConnectException.class).when(mockHttpRequester).uploadDraft(Mockito.any(), Mockito.eq(folderId));
        RetryableRequester retryableRequester = new RetryableRequester(mockHttpRequester, "put");

        ObjectNode node = mapper.createObjectNode();
        retryableRequester.put(node, folderId);

        // fail until out of attempts
        Mockito.verify(mockHttpRequester, Mockito.times(numMaxAttempts)).uploadDraft(node, folderId);
    }
}

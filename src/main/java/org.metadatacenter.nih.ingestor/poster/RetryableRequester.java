package org.metadatacenter.nih.ingestor.poster;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.resilience4j.core.functions.CheckedRunnable;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

import java.net.ConnectException;
import java.net.SocketException;
import java.time.Duration;

public class RetryableRequester {
    private RetryConfig retryConfig = RetryConfig.custom()
            .maxAttempts(3)
            .waitDuration(Duration.ofMillis(2000))
            .retryExceptions(ConnectException.class, SocketException.class)
            .failAfterMaxAttempts(true)
            .build();
    private RetryRegistry retryRegistry = RetryRegistry.of(retryConfig);
    private Retry retry;
    private HttpRequester httpRequester;

    public RetryableRequester(HttpRequester httpRequester, String name) {
        this.httpRequester = httpRequester;
        this.retry = retryRegistry.retry(name, retryConfig);
    }

    public void validate(ObjectNode cde) throws Throwable {
        CheckedRunnable validator = () -> httpRequester.validate(cde);
        CheckedRunnable retryingValidator = Retry.decorateCheckedRunnable(retry, validator);
        retryingValidator.run();
    }

    public void put(ObjectNode cde, String targetFolder) throws Throwable {
        CheckedRunnable put = () -> httpRequester.uploadDraft(cde, targetFolder);
        CheckedRunnable retryingValidator = Retry.decorateCheckedRunnable(retry, put);
        retryingValidator.run();
    }
}
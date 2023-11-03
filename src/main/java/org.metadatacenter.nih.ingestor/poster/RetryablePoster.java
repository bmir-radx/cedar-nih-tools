package org.metadatacenter.nih.ingestor.poster;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.resilience4j.core.functions.CheckedRunnable;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

import java.net.ConnectException;
import java.net.SocketException;
import java.time.Duration;

public class RetryablePoster {
    private RetryConfig retryConfig = RetryConfig.custom()
            .maxAttempts(3)
            .waitDuration(Duration.ofMillis(2000))
            .retryExceptions(ConnectException.class, SocketException.class)
            .failAfterMaxAttempts(true)
            .build();
    private RetryRegistry retryRegistry = RetryRegistry.of(retryConfig);
    private Retry retry;
    private Poster poster;

    public RetryablePoster(Poster poster, String name) {
        this.poster = poster;
        this.retry = retryRegistry.retry(name, retryConfig);
    }

    public void validate(ObjectNode cde) throws Throwable {
        CheckedRunnable validator = () -> poster.validate(cde);
        CheckedRunnable retryingValidator = Retry.decorateCheckedRunnable(retry, validator);
        retryingValidator.run();
    }

    public void put(ObjectNode cde, String targetFolder) throws Throwable {
        CheckedRunnable put = () -> poster.put(cde, targetFolder);
        CheckedRunnable retryingValidator = Retry.decorateCheckedRunnable(retry, put);
        retryingValidator.run();
    }
}
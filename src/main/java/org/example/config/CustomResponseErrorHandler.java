package org.example.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class CustomResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        // find a way to handle
        if (response.getStatusCode() == HttpStatus.FORBIDDEN) {
            System.err.println("Encountered 403 Forbidden error. Skipping this repository.");
        } else {
            System.err.println("Error: " + response.getStatusCode() + " - " + response.getStatusText());
        }
    }

}

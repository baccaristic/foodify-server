package com.foodify.server.modules.payments.konnect;

import com.foodify.server.modules.payments.konnect.dto.KonnectInitiatePaymentRequest;
import com.foodify.server.modules.payments.konnect.dto.KonnectInitiatePaymentResponse;
import com.foodify.server.modules.payments.konnect.dto.KonnectPaymentDetailsResponse;
import com.foodify.server.modules.payments.konnect.exception.KonnectApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class KonnectApiClient {

    private static final Logger log = LoggerFactory.getLogger(KonnectApiClient.class);

    private final RestTemplate restTemplate;

    public KonnectApiClient(@Qualifier("konnectRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public KonnectInitiatePaymentResponse initiatePayment(String baseUrl,
                                                          String apiKey,
                                                          KonnectInitiatePaymentRequest request) {
        if (!StringUtils.hasText(baseUrl)) {
            throw new KonnectApiException("Konnect base URL is not configured");
        }
        if (!StringUtils.hasText(apiKey)) {
            throw new KonnectApiException("Konnect API key is not configured");
        }

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/payments/init-payment")
                .build(true)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);

        HttpEntity<KonnectInitiatePaymentRequest> entity = new HttpEntity<>(request, headers);
        try {
            ResponseEntity<KonnectInitiatePaymentResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    entity,
                    KonnectInitiatePaymentResponse.class
            );
            KonnectInitiatePaymentResponse body = response.getBody();
            if (body == null || !StringUtils.hasText(body.payUrl()) || !StringUtils.hasText(body.paymentRef())) {
                throw new KonnectApiException("Konnect API response is missing payment details");
            }
            return body;
        } catch (HttpStatusCodeException ex) {
            log.error("Konnect initiate payment failed with status {}: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new KonnectApiException("Konnect API returned an error: " + ex.getStatusCode(), ex);
        } catch (RestClientException ex) {
            log.error("Konnect initiate payment failed", ex);
            throw new KonnectApiException("Failed to call Konnect API", ex);
        }
    }

    public KonnectPaymentDetailsResponse getPaymentDetails(String baseUrl,
                                                           String apiKey,
                                                           String paymentReference) {
        if (!StringUtils.hasText(baseUrl)) {
            throw new KonnectApiException("Konnect base URL is not configured");
        }
        if (!StringUtils.hasText(apiKey)) {
            throw new KonnectApiException("Konnect API key is not configured");
        }
        if (!StringUtils.hasText(paymentReference)) {
            throw new KonnectApiException("Payment reference is required");
        }

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/payments/")
                .path(paymentReference)
                .build(true)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<KonnectPaymentDetailsResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    KonnectPaymentDetailsResponse.class
            );
            KonnectPaymentDetailsResponse body = response.getBody();
            if (body == null || body.payment() == null) {
                throw new KonnectApiException("Konnect API response is missing payment payload");
            }
            return body;
        } catch (HttpStatusCodeException ex) {
            log.error("Konnect get payment details failed with status {}: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new KonnectApiException("Konnect API returned an error: " + ex.getStatusCode(), ex);
        } catch (RestClientException ex) {
            log.error("Konnect get payment details failed", ex);
            throw new KonnectApiException("Failed to call Konnect API", ex);
        }
    }
}

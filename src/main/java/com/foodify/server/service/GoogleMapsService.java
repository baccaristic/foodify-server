package com.foodify.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;
import java.util.Map;

@Service
public class GoogleMapsService {

    private final String apiKey;
    private final RestTemplate restTemplate;

    public GoogleMapsService(@Value("${google.maps.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.restTemplate = new RestTemplate();
    }

    public long  getDrivingRoute(double originLat, double originLng,
                                     double destLat, double destLng) {

        String url = "https://routes.googleapis.com/directions/v2:computeRoutes";

        // Build JSON body using coordinates
        String body = String.format(Locale.US, """
    {
      "origin": {
        "location": { "latLng": { "latitude": %f, "longitude": %f } }
      },
      "destination": {
        "location": { "latLng": { "latitude": %f, "longitude": %f } }
      },
      "travelMode": "DRIVE"
    }
    """, originLat, originLng, destLat, destLng);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", apiKey);
        headers.set("X-Goog-FieldMask", "routes.duration,routes.distanceMeters");

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        Map<String, Object> responseBody = response.getBody();

        if (responseBody == null || !responseBody.containsKey("routes")) {
            throw new RuntimeException("No routes found in Google Maps response");
        }

        // Extract first route
        Map<String, Object> route = ((java.util.List<Map<String, Object>>) responseBody.get("routes")).get(0);

        String durationStr = (String) route.get("duration"); // e.g. "2568s"

        return Long.parseLong(durationStr.replace("s", ""));
    }

    // Simple record to hold response
    public record RouteInfo(long durationSeconds, int distanceMeters) {}
}

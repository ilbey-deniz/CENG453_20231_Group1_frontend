package com.group1.frontend.utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.group1.frontend.dto.HttpRequestDto;
import com.group1.frontend.enums.PlayerColor;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


//service class for making backend requests, storing tokens, etc.
@Getter
@Setter
public class Service {
    //getters and setters

    private String token;
    private String backendURL;

    //TODO: remove this
    private String username = "polat";
    private PlayerColor playerColor = PlayerColor.RED;
    private GameRoom gameRoom = null;
    private ObjectWriter objectWriter = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .writer()
            .withDefaultPrettyPrinter();



    //make a request to the backend
    public HttpResponse<String> makeRequest(String endpoint, String method, String body) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(backendURL + endpoint))
            .header("Content-Type", "application/json")
            .method(method, HttpRequest.BodyPublishers.ofString(body))
            .build();
        return getHttpResponse(client, request);
    }
    //make a request to the backend with a token
    public HttpResponse<String> makeRequestWithToken(String endpoint, String method, HttpRequestDto body) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(backendURL + endpoint))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token)
            .method(method, HttpRequest.BodyPublishers.ofString(objectToJson(body)))
            .build();

        return getHttpResponse(client, request);
    }

    private HttpResponse<String> getHttpResponse(HttpClient client, HttpRequest request) {
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch(Exception e) {
            System.out.println(e);
        }
        if (response == null) throw new AssertionError();
        return response;
    }

    public String objectToJson(Object object) {
        String json = null;
        try {
            json = objectWriter.writeValueAsString(object);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        if (json == null) throw new AssertionError();
        return json;
    }

}

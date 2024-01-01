package com.group1.frontend.utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.group1.frontend.dto.httpDto.HttpRequestDto;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.group1.frontend.constants.ApplicationConstants.WEBSOCKET_GAME_URL;


//service class for making backend requests, storing tokens, etc.
@Getter
@Setter
public class Service {
    //getters and setters

    private String token;
    private String backendURL;
    private GenericWebsocketClient client;

    private String username;
    private GameRoom gameRoom = null;
    private ObjectWriter objectWriter = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .writer()
            .withDefaultPrettyPrinter();
    private ObjectMapper objectMapper = new ObjectMapper();


    //make a request to the backend
    public HttpResponse<String> makeRequest(String endpoint, String method, HttpRequestDto body) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(backendURL + endpoint))
            .header("Content-Type", "application/json")
            .method(method, HttpRequest.BodyPublishers.ofString(objectToJson(body)))
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        if (json == null) throw new AssertionError();
        return json;
    }
    public Object jsonToObject(String json, Class<?> classType) {
        Object object = null;
        try {
            object = objectMapper.readValue(json, classType);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        if (object == null) throw new AssertionError();
        return object;
    }

    public void createGameRoom() {
        gameRoom = new GameRoom();
    }
    public void connectToGameRoom(GenericWebsocketClient.OnMessageHandler handler) {
        URI uri = null;
        try {
            uri = new URI(WEBSOCKET_GAME_URL + gameRoom.getRoomCode());
            client = new GenericWebsocketClient(uri);
            client.addHeader("Authorization", "Bearer " + token);
            client.setMessageHandler(handler);
            client.connect();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void setWebsocketHandler(GenericWebsocketClient.OnMessageHandler handler) {
        client.setMessageHandler(handler);
    }

    public void sendWebsocketMessage(String message) {
        client.send(message);
    }
    public void disconnectFromGameRoom() {
        client.close();
    }
}

package com.group1.frontend.utils;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


//service class for making backend requests, storing tokens, etc.
public class Service {
    private String token;
    private String backendURL;
    private String username;

    //getters and setters
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setUsername(String username) {this.username = username;}
    public void setBackendURL(String backendURL) {
        this.backendURL = backendURL;
    }
    public String getBackendURL() {
        return backendURL;
    }
    public String getUsername() {return username;}

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
    public HttpResponse<String> makeRequestWithToken(String endpoint, String method, String body) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(backendURL + endpoint))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token)
            .method(method, HttpRequest.BodyPublishers.ofString(body))
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

}

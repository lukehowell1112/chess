package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final String serverUrl;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;
    }

    private HttpRequest.BodyPublisher buildBody(Object body) {
        if (body == null) {
            return HttpRequest.BodyPublishers.noBody();
        }
        return HttpRequest.BodyPublishers.ofString(gson.toJson(body));
    }

    private String readErrorMessage(String body) {
        try {
            JsonObject json = gson.fromJson(body, JsonObject.class);
            return json.get("message").getAsString();
        } catch (RuntimeException ex) {
            return "Something went wrong";
        }
    }

    private <T> T makeRequest(String method, String path, Object body, String authToken,
                              Class<T> responseClass) throws ResponseException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, buildBody(body));
        if (body != null) {
            builder.header("Content-Type", "application/json");
        }
        if (authToken != null) {
            builder.header("authorization", authToken);
        }

        HttpResponse<String> response;
        try {
            response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            throw new ResponseException(500, "Could not reach the server");
        }

        if (response.statusCode() != 200) {
            throw new ResponseException(response.statusCode(), readErrorMessage(response.body()));
        }
        if (responseClass == null) {
            return null;
        }
        return gson.fromJson(response.body(), responseClass);
    }
}

package server;

import com.google.gson.Gson;
import exception.ResponseException;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String serverUrl;

    ServerFacade(String url) {
        serverUrl = url;
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}

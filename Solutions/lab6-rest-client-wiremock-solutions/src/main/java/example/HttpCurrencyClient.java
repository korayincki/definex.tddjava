package example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpCurrencyClient {
    private final String baseUrl;
    private final HttpClient http = HttpClient.newHttpClient();

    public HttpCurrencyClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public double convert(String from, String to, double amount) {
        try {
            var req = HttpRequest
                    .newBuilder(URI.create(baseUrl + "/convert?from=" + from + "&to=" + to + "&amount=" + amount)).GET()
                    .build();
            var res = http.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200)
                return Double.parseDouble(res.body());
            if (res.statusCode() == 404)
                throw new IllegalArgumentException("unsupported currency");
            throw new IllegalStateException("remote error: " + res.statusCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

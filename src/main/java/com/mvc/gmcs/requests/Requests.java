package com.mvc.gmcs.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mvc.gmcs.dataprofile.JsonBodyHandler;
import com.mvc.gmcs.dataprofile.Snap;
import com.mvc.gmcs.playback.Playback;
import com.mvc.gmcs.params.Params;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Requests {
    private static final HttpClient client = HttpClient.newHttpClient();

    private static final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    private static final JsonBodyHandler<Playback> playbackJsonBodyHandler = new JsonBodyHandler<>(Playback.class);
    private static final JsonBodyHandler<Params> paramsJsonBodyHandler = new JsonBodyHandler<>(Params.class);


    public static Playback getPlayback() throws IOException, InterruptedException {
        // create a client
//        var client = HttpClient.newHttpClient();

        // create a request
        var getRequest = HttpRequest.newBuilder(
                URI.create("http://localhost:8080/playback"))
                .GET().build();

        // use the client to send the request
        var getResponse =
                client.send(getRequest, playbackJsonBodyHandler);

        // the response body:
        return getResponse.body().get();
    }

    public static void putReset() throws IOException, InterruptedException {
        String requestBody = ow.writeValueAsString(new Playback(true,false, false));
//        System.out.println(requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/playback"))
                .headers("Accept", "application/json",
                        "Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    public static void putEquilibrate(boolean pause, boolean reset) throws IOException, InterruptedException {
        String requestBody = ow.writeValueAsString(new Playback(pause,reset, false));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/playback"))
                .headers("Accept", "application/json",
                        "Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void putSnap(Snap snap) throws IOException, InterruptedException {
        String requestBody = ow.writeValueAsString(snap);
//        byte[] requestBody = ow.writeValueAsBytes(new Snap(1,2));
//        System.out.println(requestBody);
//        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/snap"))
                .headers("Accept", "application/json",
                        "Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
//        HttpResponse<String> response = client.send(request,
//                HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());
    }

    public static Params getParams() throws IOException, InterruptedException {
        // create a request
        var getRequest = HttpRequest.newBuilder(
                URI.create("http://localhost:8080/params"))
                .GET().build();

        // use the client to send the request
        var getResponse =
                client.send(getRequest, paramsJsonBodyHandler);

        // the response body:
        return getResponse.body().get();
    }

    public static void putParams(Params params) throws IOException, InterruptedException {
        String requestBody = ow.writeValueAsString(params);

        System.out.println(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/params"))
                .headers("Accept", "application/json",
                        "Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}

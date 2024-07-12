package com.aluracursos.biblioApp.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {
    private final URI url = URI.create("http://gutendex.com/books/?search=");

    public String obtener(String titulo) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + titulo.replace(" ", "+").toLowerCase()))
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
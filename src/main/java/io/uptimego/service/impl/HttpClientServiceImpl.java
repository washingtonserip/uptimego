package io.uptimego.service.impl;

import io.uptimego.service.HttpClientService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HttpClientServiceImpl implements HttpClientService {
    private final OkHttpClient httpClient = new OkHttpClient();

    @Override
    public Response executeGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return httpClient.newCall(request).execute();
    }

    @Override
    public Response executeHeadRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .head()
                .build();
        return httpClient.newCall(request).execute();
    }
}

package io.uptimego.service;

import okhttp3.Response;
import java.io.IOException;

public interface HttpClientService {
    Response executeGetRequest(String url) throws IOException;
    Response executeHeadRequest(String url) throws IOException;
}

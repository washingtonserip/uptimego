package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUptimeStrategy implements UptimeStrategy {
    private final OkHttpClient httpClient = new OkHttpClient();

    @Override
    public Uptime checkUptime(Heartbeat heartbeat) {
        Uptime uptime = new Uptime();
        uptime.setHeartbeatId(heartbeat.getId());

        Request request = new Request.Builder()
                .url(heartbeat.getUrl())
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                uptime.setStatus("up");
            } else {
                uptime.setStatus("down");
            }
        } catch (Exception e) {
            uptime.setStatus("down");
        }

        return uptime;
    }
}

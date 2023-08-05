package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;
import io.uptimego.service.HttpClientService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
@NoArgsConstructor
public class HttpUptimeStrategy implements UptimeStrategy {

    @Autowired
    private HttpClientService httpClientService;

    @Override
    public String getType() {
        return "HTTP";
    }

    @Override
    public Uptime checkUptime(Heartbeat heartbeat) {
        Uptime uptime = new Uptime();
        uptime.setHeartbeatId(heartbeat.getId());

        try {
            Response response = httpClientService.executeGetRequest(heartbeat.getUrl());
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

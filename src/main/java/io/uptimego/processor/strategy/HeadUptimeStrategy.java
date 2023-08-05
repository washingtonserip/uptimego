package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatType;
import io.uptimego.model.Uptime;
import io.uptimego.service.HttpClientService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import okhttp3.Response;

@AllArgsConstructor
@NoArgsConstructor
public class HeadUptimeStrategy implements UptimeStrategy {

    private HttpClientService httpClientService;

    @Override
    public String getType() {
        return "HEAD";
    }

    @Override
    public Uptime checkUptime(Heartbeat heartbeat) {
        Uptime uptime = new Uptime();
        uptime.setHeartbeatId(heartbeat.getId());

        try {
            Response response = httpClientService.executeHeadRequest(heartbeat.getUrl());
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

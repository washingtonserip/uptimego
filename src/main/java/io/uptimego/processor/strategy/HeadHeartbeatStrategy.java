package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatStatus;
import io.uptimego.model.UptimeConfig;
import io.uptimego.service.HttpClientService;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HeadHeartbeatStrategy implements HeartbeatStrategy {

    @Autowired
    private HttpClientService httpClientService;

    @Override
    public String getType() {
        return "HEAD";
    }

    @Override
    public Heartbeat getHeartbeat(UptimeConfig uptimeConfig) {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setUptimeConfig(uptimeConfig);

        try {
            Response response = httpClientService.executeHeadRequest(uptimeConfig.getUrl());
            if (response.isSuccessful()) {
                heartbeat.setStatus(HeartbeatStatus.UP);
            } else {
                heartbeat.setStatus(HeartbeatStatus.DOWN);
            }
        } catch (Exception e) {
            heartbeat.setStatus(HeartbeatStatus.DOWN);
        }

        return heartbeat;
    }
}

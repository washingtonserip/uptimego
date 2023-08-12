package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatStatus;
import io.uptimego.model.UptimeConfig;
import io.uptimego.service.HttpClientService;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HttpHeartbeatStrategy implements HeartbeatStrategy {

    @Autowired
    private HttpClientService httpClientService;

    @Override
    public String getType() {
        return "HTTP";
    }

    @Override
    public Heartbeat getHeartbeat(UptimeConfig uptimeConfig) {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setUptimeConfig(uptimeConfig);

        try {
            Response response = httpClientService.executeGetRequest(uptimeConfig.getUrl());
            if (response.isSuccessful()) {
                heartbeat.setLatency((int) (response.receivedResponseAtMillis() - response.sentRequestAtMillis()));
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

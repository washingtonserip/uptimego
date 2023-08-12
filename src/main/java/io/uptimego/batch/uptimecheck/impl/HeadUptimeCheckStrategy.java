package io.uptimego.batch.uptimecheck.impl;

import io.uptimego.batch.uptimecheck.UptimeCheckStrategy;
import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatStatus;
import io.uptimego.model.UptimeConfig;
import io.uptimego.service.HttpClientService;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HeadUptimeCheckStrategy implements UptimeCheckStrategy {

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

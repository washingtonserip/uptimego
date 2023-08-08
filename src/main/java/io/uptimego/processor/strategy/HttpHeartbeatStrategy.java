package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatDetails;
import io.uptimego.model.UptimeConfig;
import io.uptimego.service.HttpClientService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
@NoArgsConstructor
public class HttpHeartbeatStrategy implements HeartbeatStrategy {

    @Autowired
    private HttpClientService httpClientService;

    @Override
    public String getType() {
        return "HTTP";
    }

    @Override
    public Heartbeat getHeartbeat(UptimeConfig uptimeConfig) {
        HeartbeatDetails details = new HeartbeatDetails();
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setUptimeConfig(uptimeConfig);

        try {
            Response response = httpClientService.executeGetRequest(uptimeConfig.getUrl());
            details.setResponseCode(response.code());
            if (response.isSuccessful()) {
                heartbeat.setStatus("up");
            } else {
                heartbeat.setStatus("down");
            }
        } catch (Exception e) {
            details.setStatusReason(e.getMessage());
            heartbeat.setStatus("down");
        }
        heartbeat.setDetails(details);

        return heartbeat;
    }
}

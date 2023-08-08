package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatDetails;
import io.uptimego.model.UptimeConfig;
import io.uptimego.service.HttpClientService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import okhttp3.Response;

@AllArgsConstructor
@NoArgsConstructor
public class HeadHeartbeatStrategy implements HeartbeatStrategy {

    private HttpClientService httpClientService;

    @Override
    public String getType() {
        return "HEAD";
    }

    @Override
    public Heartbeat getHeartbeat(UptimeConfig uptimeConfig) {
        HeartbeatDetails details = new HeartbeatDetails();
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setUptimeConfig(uptimeConfig);

        try {
            Response response = httpClientService.executeHeadRequest(uptimeConfig.getUrl());
            details.setResponseCode(response.code());
            if (response.isSuccessful()) {
                heartbeat.setStatus("up");
            } else {
                details.setStatusReason("Response code is not 2xx");
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

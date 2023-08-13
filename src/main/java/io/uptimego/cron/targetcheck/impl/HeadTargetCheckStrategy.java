package io.uptimego.cron.targetcheck.impl;

import io.uptimego.cron.targetcheck.TargetCheckStrategy;
import io.uptimego.model.Pulse;
import io.uptimego.model.PulseStatus;
import io.uptimego.model.Target;
import io.uptimego.service.HttpClientService;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HeadTargetCheckStrategy implements TargetCheckStrategy {

    @Autowired
    private HttpClientService httpClientService;

    @Override
    public String getType() {
        return "HEAD";
    }

    @Override
    public Pulse getPulse(Target target) {
        Pulse pulse = new Pulse();
        pulse.setTarget(target);

        try {
            Response response = httpClientService.executeHeadRequest(target.getUrl());
            if (response.isSuccessful()) {
                pulse.setLatency((int) (response.receivedResponseAtMillis() - response.sentRequestAtMillis()));
                pulse.setStatus(PulseStatus.UP);
            } else {
                pulse.setStatus(PulseStatus.DOWN);
            }
        } catch (Exception e) {
            pulse.setStatus(PulseStatus.DOWN);
        }

        return pulse;
    }
}

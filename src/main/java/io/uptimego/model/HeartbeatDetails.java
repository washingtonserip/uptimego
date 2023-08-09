package io.uptimego.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeartbeatDetails {
    private double responseTime;
    private int responseCode;
    private String ipAddress;
    private String statusReason;
}

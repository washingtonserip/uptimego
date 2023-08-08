package io.uptimego.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeartbeatDetails {
    private double responseTime;
    private int responseCode;
    private String ipAddress;
    private String statusReason;
}

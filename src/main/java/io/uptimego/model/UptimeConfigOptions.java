package io.uptimego.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UptimeConfigOptions {
    private String host;
    private int port;
    private String emailTo;
}

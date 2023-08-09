package io.uptimego.model;

public enum UptimeConfigType {
    HTTP, HEAD, TCP, DNS, SMTP, PING, PUSH, CUSTOM,
    // only for unit tests
    UNKNOWN
}

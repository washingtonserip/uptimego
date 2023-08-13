package io.uptimego.model;

public enum TargetType {
    HTTP, HEAD, TCP, DNS, SMTP, PING, PUSH, CUSTOM,
    // only for unit tests
    UNKNOWN
}

package io.uptimego.model;

import io.uptimego.util.CryptoConverter;
import jakarta.persistence.Convert;

public class UptimeConfigOptions {
    private String host;
    private int port;
    private String username;
    @Convert(converter = CryptoConverter.class)
    private String password;
    private String emailTo;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    @Override
    public String toString() {
        return "UptimeConfigOptions{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", emailTo='" + emailTo + '\'' +
                '}';
    }
}

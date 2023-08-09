package io.uptimego.model;

public class HeartbeatDetails {
    private double responseTime;
    private int responseCode;
    private String ipAddress;
    private String statusReason;

    public double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    @Override
    public String toString() {
        return "HeartbeatDetails{" +
                "responseTime=" + responseTime +
                ", responseCode=" + responseCode +
                ", ipAddress='" + ipAddress + '\'' +
                ", statusReason='" + statusReason + '\'' +
                '}';
    }
}

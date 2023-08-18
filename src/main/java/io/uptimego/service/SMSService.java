package io.uptimego.service;

public interface SMSService {
    boolean sendSMS(String phoneNumber, String message);
}

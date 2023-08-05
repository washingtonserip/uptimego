package io.uptimego.service;

public interface EmailService {
    public void sendEmail(String host, int port, String emailFrom, String emailTo) throws Exception;
}
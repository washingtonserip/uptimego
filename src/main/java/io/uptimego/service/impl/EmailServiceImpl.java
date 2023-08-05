package io.uptimego.service.impl;

import io.uptimego.service.EmailService;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {
    public void sendEmail(String host, int port, String emailFrom, String emailTo) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        Session session = Session.getDefaultInstance(properties, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailFrom));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
        Transport.send(message);
    }
}

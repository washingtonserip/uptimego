package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatOptions;
import io.uptimego.model.Uptime;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SmtpUptimeStrategy implements UptimeStrategy {

    @Override
    public Uptime checkUptime(Heartbeat heartbeat) {
        Uptime uptime = new Uptime();
        uptime.setHeartbeatId(heartbeat.getId());

        try {
            HeartbeatOptions options = heartbeat.getOptions();
            String emailTo = options.getEmailTo() != null ? options.getEmailTo() : "test@test.com";
            Properties properties = new Properties();
            properties.put("mail.smtp.host", options.getHost());
            properties.put("mail.smtp.port", options.getPort());
            Session session = Session.getDefaultInstance(properties, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("test@uptimego.io"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            Transport.send(message);
            uptime.setStatus("up");
        } catch (Exception e) {
            uptime.setStatus("down");
        }

        return uptime;
    }
}
package io.uptimego.service.impl;

import io.uptimego.cron.sendalert.impl.MailNotificationStrategy;
import io.uptimego.service.SMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SMSServiceImpl implements SMSService {
    private static final Logger log = LoggerFactory.getLogger(SMSServiceImpl.class);

    @Value("${twilio.phoneNumber}")
    private String fromPhoneNumber;

    public boolean sendSMS(String toPhoneNumber, String messageContent) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    messageContent
            ).create();

            log.info("SMS sent successfully: " + message.getSid());
            return true;
        } catch (Exception e) {
            log.error("Failed to send SMS: ", e.getMessage());
            return false;
        }
    }
}

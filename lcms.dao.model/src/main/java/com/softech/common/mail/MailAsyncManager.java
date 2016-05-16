package com.softech.common.mail;

import java.io.InputStream;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MailAsyncManager {

    @Async
    public void send(String[] toAddr,
            String[] ccAddr,
            String fromAddr,
            String fromAddrPersonalName,
            String subject,
            String body) {

        SendMailService.sendSMTPMessage(toAddr, ccAddr, fromAddr,
                fromAddrPersonalName, subject, body);
    }

    @Async
    public void send(String[] toAddr,
            String[] ccAddr,
            String fromAddr,
            String fromAddrPersonalName,
            String subject,
            String body,
            byte[][] attachments,
            String[] fileNames) {

        SendMailService.sendSMTPMessage(toAddr, ccAddr, fromAddr,
                fromAddrPersonalName, subject, body, attachments[0], fileNames[0]);

    }
}

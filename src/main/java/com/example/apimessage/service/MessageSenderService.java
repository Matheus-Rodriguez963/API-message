package com.example.apimessage.service;

import com.example.apimessage.config.TwilioProperties;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderService {

    private static final Logger logger = LoggerFactory.getLogger(MessageSenderService.class);
    private final TwilioProperties twilioProperties;

    public MessageSenderService(TwilioProperties twilioProperties) {
        this.twilioProperties = twilioProperties;
    }

    public void sendWhatsApp(String recipient, String message, String whatsappFrom) {
        if (isTwilioConfigured()) {
            String from = resolveWhatsappFrom(whatsappFrom);
            if (from == null || from.isBlank()) {
                logger.warn("Twilio configurado, mas número de origem do WhatsApp não informado.");
                return;
            }
            Twilio.init(twilioProperties.getAccountSid(), twilioProperties.getAuthToken());
            Message.creator(
                    new PhoneNumber(formatWhatsAppNumber(recipient)),
                    new PhoneNumber(formatWhatsAppNumber(from)),
                    message)
                .create();
            return;
        }
        logger.info("Enviando WhatsApp para {}: {}", recipient, message);
    }

    public void sendEmail(String recipient, String message) {
        logger.info("Enviando e-mail para {}: {}", recipient, message);
    }

    private boolean isTwilioConfigured() {
        return twilioProperties.getAccountSid() != null
            && !twilioProperties.getAccountSid().isBlank()
            && twilioProperties.getAuthToken() != null
            && !twilioProperties.getAuthToken().isBlank();
    }

    private String resolveWhatsappFrom(String whatsappFrom) {
        if (whatsappFrom != null && !whatsappFrom.isBlank()) {
            return whatsappFrom;
        }
        return twilioProperties.getWhatsappFrom();
    }

    private String formatWhatsAppNumber(String number) {
        if (number == null) {
            return null;
        }
        if (number.startsWith("whatsapp:")) {
            return number;
        }
        return "whatsapp:" + number;
    }
}

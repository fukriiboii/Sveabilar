package com.sveabilar.api.features.email.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Value("${app.mail.reply-to}")
    private String replyToAddress;

    @Async
    public void sendBookingConfirmation(
            String to,
            String customerName,
            String serviceName,
            String date,
            String time,
            String address
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setReplyTo(replyToAddress);
        message.setSubject("Bekräftelse för din bokning");
        message.setText(
            "Hej " + customerName + ",\n\n" +
            "Din bokning är nu bekräftad.\n\n" +
            "Tjänst: " + serviceName + "\n" +
            "Datum: " + date + "\n" +
            "Tid: " + time + "\n" +
            "Adress: " + address + "\n\n" +

            "Vi ser fram emot att hjälpa dig.\n\n" +
            
            "För avbokning kontakta: 073-397 64 25.\n" +
            "Vid avbokning mindre än 24 timmar före bokad tid tillkommer en avgift på 100 kr.\n\n" +

            "Vänliga hälsningar,\n" +
            "Sveabilar och Däck AB\n" +
            fromAddress
        );

        try {
            mailSender.send(message);
            logger.info("Booking confirmation email sent for recipient={}", to);
        } catch (Exception exception) {
            logger.error("Booking confirmation email failed for recipient={}", to, exception);
        }
    }
}
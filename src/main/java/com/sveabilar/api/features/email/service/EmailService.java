package com.sveabilar.api.features.email.service;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger logger =
            LoggerFactory.getLogger(EmailService.class);

    private final Resend resend;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Value("${app.mail.reply-to}")
    private String replyToAddress;

    public void sendBookingConfirmation(
            String to,
            String customerName,
            String serviceName,
            String date,
            String time,
            String address
    ) {

        String text =
                "Hej " + customerName + ",\n\n" +
                "Din bokning är nu bekräftad.\n\n" +
                "Tjänst: " + serviceName + "\n" +
                "Datum: " + date + "\n" +
                "Tid: " + time + "\n" +
                "Adress: " + address + "\n\n" +
                "Vi ser fram emot att hjälpa dig.\n\n" +
                "För avbokning kontakta: 073-397 64 25.\n" +
                "Vid avbokning mindre än 24 timmar före bokad tid " +
                "tillkommer en avgift på 100 kr.\n\n" +
                "Vänliga hälsningar,\n" +
                "Sveabilar och Däck AB\n" +
                fromAddress;

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(fromAddress)
                .to(to)
                .replyTo(replyToAddress)
                .subject("Bekräftelse för din bokning")
                .text(text)
                .build();

        try {
            resend.emails().send(params);

            logger.info(
                    "Booking confirmation email sent for recipient={}",
                    to
            );

        } catch (Exception exception) {
            logger.error(
                    "Booking confirmation email failed for recipient={}",
                    to,
                    exception
            );
        }
    }
}
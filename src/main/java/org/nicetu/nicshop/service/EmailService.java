package org.nicetu.nicshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.Assert;

@Service
public class EmailService {
    private final JavaMailSender emailSender;
    private final String fromAddress;

    @Autowired
    public EmailService(JavaMailSender emailSender,
                        @Value("${spring.mail.username}") String fromAddress) {
        this.emailSender = emailSender;
        this.fromAddress = fromAddress;
    }

    public void sendSimpleEmail(String toAddress, String subject, String message) {
        Assert.hasText(toAddress, "To address must not be null or empty");
        Assert.hasText(subject, "Subject must not be null or empty");
        Assert.hasText(message, "Message must not be null or empty");

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromAddress);
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        try {
            emailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new EmailSendingException("Failed to send email to " + toAddress, e);
        }
    }
}

class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}

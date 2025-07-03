package org.nicetu.nicshop.service.api;

public interface EmailService {
    void sendSimpleEmail(String toAddress, String subject, String message);
}

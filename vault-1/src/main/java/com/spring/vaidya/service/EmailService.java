package com.spring.vaidya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service class for handling email operations.
 */
@Service("emailService")
public class EmailService {

    private final JavaMailSender javaMailSender;

    /**
     * Constructor-based dependency injection for JavaMailSender.
     * 
     * @param javaMailSender The JavaMailSender instance used for sending emails.
     */
    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Sends an email asynchronously.
     * 
     * @param email The email message to be sent.
     */
    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }
}

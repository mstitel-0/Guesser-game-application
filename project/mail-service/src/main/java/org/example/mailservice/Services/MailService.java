package org.example.mailservice.Services;

import org.example.mailservice.Models.Confirmation;
import org.example.mailservice.Repositories.ConfirmationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String SENDER_MAIL;

    @Value("${application.services.authentication.base-url}")
    private String AUTH_SERVICE_HOST;

    private static final String AUTH_SERVICE_CONFIRMATION_PATH = "/auth/confirm?token=";

    private final JavaMailSender mailSender;

    private final ConfirmationRepository confirmationRepository;

    public MailService(JavaMailSender mailSender, ConfirmationRepository confirmationRepository) {
        this.mailSender = mailSender;
        this.confirmationRepository = confirmationRepository;
    }

    public void sendEmail(String email, String confirmationToken) {
        Confirmation confirmation = new Confirmation(email, LocalDateTime.now());
        confirmationRepository.save(confirmation);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER_MAIL);
        message.setTo(email);
        message.setSubject("Mail confirmation");
        message.setText("Please, follow this link to confirm your account.\n"
                + "https://gull-tidy-primarily.ngrok-free.giapp" + AUTH_SERVICE_CONFIRMATION_PATH + confirmationToken);
        mailSender.send(message);
    }
}

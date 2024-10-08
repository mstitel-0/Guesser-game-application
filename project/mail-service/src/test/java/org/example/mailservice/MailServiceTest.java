package org.example.mailservice;

import static org.mockito.Mockito.*;

import org.example.mailservice.Models.Confirmation;
import org.example.mailservice.Repositories.ConfirmationRepository;
import org.example.mailservice.Services.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;


public class MailServiceTest {
    @InjectMocks
    private MailService mailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private ConfirmationRepository confirmationRepository;


    private static final String EMAIL = "test@gmail.com";
    private static final String CONFIRMATION_TOKEN = "token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(mailService, "senderMail", "sender@gmail.com");
    }

    @Test
    void testSendEmail() {
        mailService.sendEmail(EMAIL, CONFIRMATION_TOKEN);

        verify(confirmationRepository).save(any(Confirmation.class));
        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }
}

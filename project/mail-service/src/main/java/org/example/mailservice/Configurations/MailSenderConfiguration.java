package org.example.mailservice.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSenderConfiguration {

    @Value("${spring.mail.host}")
    private String HOST;
    @Value("${spring.mail.port}")
    private int PORT;
    @Value("${spring.mail.username}")
    private String USERNAME;
    @Value("${spring.mail.password}")
    private String PASSWORD;
    @Value("${spring.mail.default-encoding}")
    private String ENCODING;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean AUTHENTICATION;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean STARTTLS_ENABLE;

    @Bean
    public JavaMailSender getJavaMailSender() {
       JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
       mailSender.setHost(HOST);
       mailSender.setPort(PORT);
       mailSender.setDefaultEncoding(ENCODING);
       mailSender.setUsername(USERNAME);
       mailSender.setPassword(PASSWORD);

       Properties properties = mailSender.getJavaMailProperties();
       properties.put("mail.smtp.auth", AUTHENTICATION);
       properties.put("mail.smtp.starttls.enable", STARTTLS_ENABLE);
       return mailSender;
    }
}

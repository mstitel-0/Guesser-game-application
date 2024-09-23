package com.example.mailservice.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSenderConfiguration {

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.default-encoding}")
    private String encoding;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean authentication;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttls;

    @Bean
    public JavaMailSender getJavaMailSender() {
       JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
       mailSender.setHost(host);
       mailSender.setPort(port);
       mailSender.setDefaultEncoding(encoding);
       mailSender.setUsername(username);
       mailSender.setPassword(password);

       Properties properties = mailSender.getJavaMailProperties();
       properties.put("mail.smtp.auth", authentication);
       properties.put("mail.smtp.starttls.enable", starttls);
       return mailSender;
    }
}

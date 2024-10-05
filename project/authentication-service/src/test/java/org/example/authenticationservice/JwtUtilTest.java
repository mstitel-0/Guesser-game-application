package org.example.authenticationservice;

import static org.junit.jupiter.api.Assertions.*;

import org.example.authenticationservice.Services.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

public class JwtUtilTest {
    @InjectMocks
    private JwtUtil jwtUtil;

    private String TEST_JWT_SECRET = "12345678901234567890123456789012";

    private final String EMAIL = "example@gmail.com";

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtUtil, "JWT_SECRET", TEST_JWT_SECRET);
    }

    @Test
    void testGenerateAccessToken() {
        String token = jwtUtil.generateAccessToken(EMAIL);

        assertNotNull(token);
    }

    @Test
    void testGenerateEmailConfirmationToken() {
        String token = jwtUtil.generateEmailConfirmationToken(EMAIL);

        assertNotNull(token);
    }

    @Test
    void testVerifyEmailConfirmationToken() {
        String token = jwtUtil.generateEmailConfirmationToken(EMAIL);
        String emailToCheck = jwtUtil.verifyEmailConfirmationToken(token);

        assertNotNull(token);
        assertEquals(EMAIL, emailToCheck);
    }
}

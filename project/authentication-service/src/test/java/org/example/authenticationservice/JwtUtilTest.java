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

    private static final String TEST_JWT_SECRET = "12345678901234567890123456789012";

    private static final String EMAIL = "example@gmail.com";

    private static final Long USER_ID = 12312423L;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtUtil, "JWT_SECRET", TEST_JWT_SECRET);
    }

    @Test
    void testGenerateAccessToken() {
        String token = jwtUtil.generateAccessToken(EMAIL, USER_ID);

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

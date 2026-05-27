package hr.algebra.glowlog.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secret", "testSecretKey12345678901234567890");
        ReflectionTestUtils.setField(jwtService, "accessExpiryMs", 3600000L);
    }

    @Test
    void testGenerateAndExtractToken() {
        UserDetails user = mock(UserDetails.class);
        when(user.getUsername()).thenReturn("testuser");
        when(user.getAuthorities()).thenReturn(List.of());

        String token = jwtService.generateAccessToken(user);

        assertNotNull(token);
        assertEquals("testuser", jwtService.extractUsername(token));
    }

    @Test
    void testIsValidWithCorrectUser() {
        UserDetails user = mock(UserDetails.class);
        when(user.getUsername()).thenReturn("testuser");
        when(user.getAuthorities()).thenReturn(List.of());

        String token = jwtService.generateAccessToken(user);

        assertTrue(jwtService.isValid(token, user));
    }

    @Test
    void testIsInvalidWithExpiredToken() {
        UserDetails user = mock(UserDetails.class);

        String expiredToken = jwtService.generateTokenForTest(
                "testuser",
                new Date(System.currentTimeMillis() - 3600000)
        );

        assertFalse(jwtService.isValid(expiredToken, user));
    }
}
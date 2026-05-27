package hr.algebra.glowlog.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import hr.algebra.glowlog.entity.RefreshToken;
import hr.algebra.glowlog.entity.User;
import hr.algebra.glowlog.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshExpiryMs", 600000L);
    }

    @Test
    void testCreateRefreshToken() {
        User user = new User();
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArguments()[0]);

        RefreshToken token = refreshTokenService.createRefreshToken(user);

        assertNotNull(token);
        assertNotNull(token.getToken());
        assertFalse(token.isRevoked());
        verify(refreshTokenRepository).deleteByUser(user);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void testFindByToken() {
        RefreshToken token = new RefreshToken();
        when(refreshTokenRepository.findByToken("abc")).thenReturn(Optional.of(token));

        Optional<RefreshToken> result = refreshTokenService.findByToken("abc");

        assertTrue(result.isPresent());
        assertEquals(token, result.get());
    }

    @Test
    void testIsValid() {
        RefreshToken validToken = new RefreshToken();
        validToken.setRevoked(false);
        validToken.setExpiryDate(Instant.now().plusSeconds(100));

        RefreshToken revokedToken = new RefreshToken();
        revokedToken.setRevoked(true);
        revokedToken.setExpiryDate(Instant.now().plusSeconds(100));

        RefreshToken expiredToken = new RefreshToken();
        expiredToken.setRevoked(false);
        expiredToken.setExpiryDate(Instant.now().minusSeconds(100));

        assertTrue(refreshTokenService.isValid(validToken));
        assertFalse(refreshTokenService.isValid(revokedToken));
        assertFalse(refreshTokenService.isValid(expiredToken));
    }

    @Test
    void testRevokeByUser() {
        User user = new User();
        refreshTokenService.revokeByUser(user);
        verify(refreshTokenRepository).deleteByUser(user);
    }
}
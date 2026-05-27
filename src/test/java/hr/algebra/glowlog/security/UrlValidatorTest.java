package hr.algebra.glowlog.security;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class UrlValidatorTest {

    private final UrlValidator validator = new UrlValidator();

    @Test
    void testValidUrl() {
        assertDoesNotThrow(() -> validator.validate("https://google.com"));
    }

    @Test
    void testBlockedHostThrowsException() {
        assertThrows(SecurityException.class, () -> validator.validate("http://localhost"));
        assertThrows(SecurityException.class, () -> validator.validate("http://127.0.0.1"));
    }
}
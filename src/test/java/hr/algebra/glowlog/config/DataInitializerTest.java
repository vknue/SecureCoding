package hr.algebra.glowlog.config;

import static org.mockito.Mockito.*;

import hr.algebra.glowlog.repository.ProductRepository;
import hr.algebra.glowlog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock private UserRepository userRepository;
    @Mock private ProductRepository productRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private ApplicationArguments args;

    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        dataInitializer = new DataInitializer(userRepository, productRepository, passwordEncoder);
    }

    @Test
    void testRunDoesNothingIfUsersExist() {
        when(userRepository.count()).thenReturn(1L);

        dataInitializer.run(args);

        verify(userRepository, never()).save(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void testRunInitializesDataIfRepositoryEmpty() {
        when(userRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode(any())).thenReturn("encoded");

        dataInitializer.run(args);

        verify(userRepository, times(2)).save(any());
        verify(productRepository, atLeastOnce()).save(any());
    }
}
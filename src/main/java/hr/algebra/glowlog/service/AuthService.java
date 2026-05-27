package hr.algebra.glowlog.service;

import hr.algebra.glowlog.dto.Dto;
import hr.algebra.glowlog.dto.ProductDto;
import hr.algebra.glowlog.entity.Product;
import hr.algebra.glowlog.entity.User;
import hr.algebra.glowlog.enums.Role;
import hr.algebra.glowlog.repository.UserRepository;
import hr.algebra.glowlog.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



    public void register(Dto.RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already taken.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered.");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setEnabled(true);

        userRepository.save(user);
    }

    public Dto.TokenResponse login(Dto.LoginRequest request, JwtService jwtService, org.springframework.security.core.userdetails.UserDetailsService userDetailsService) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = "mock-refresh-token"; // Replace with your logic

        return Dto.TokenResponse.of(accessToken, refreshToken, 3600000);
    }


}

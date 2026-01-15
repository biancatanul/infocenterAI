package ro.uvt.infochat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.uvt.infochat.dto.LoginRequest;
import ro.uvt.infochat.dto.LoginResponse;
import ro.uvt.infochat.model.User;
import ro.uvt.infochat.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody LoginRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            return ResponseEntity.ok(
                    new LoginResponse(false, null, "Email already registered")
            );
        }

        if (request.getPassword().length() < 6) {
            return ResponseEntity.ok(
                    new LoginResponse(false, null, "Password must be at least 6 characters")
            );
        }

        if (!request.getRole().equals("STUDENT") && !request.getRole().equals("ADMIN")) {
            return ResponseEntity.ok(
                    new LoginResponse(false, null, "Invalid role")
            );
        }

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole());

        userRepository.save(newUser);

        return ResponseEntity.ok(
                new LoginResponse(true, newUser.getId(), "Registration successful")
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.ok(
                    new LoginResponse(false, null, "User not found")
            );
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.ok(
                    new LoginResponse(false, null, "Incorrect password")
            );
        }

        if (!user.getRole().equals(request.getRole())) {
            return ResponseEntity.ok(
                    new LoginResponse(false, null, "Role mismatch")
            );
        }

        return ResponseEntity.ok(
                new LoginResponse(true, user.getId(), "Login successful")
        );
    }
}
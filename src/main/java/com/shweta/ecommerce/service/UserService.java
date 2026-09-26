package com.shweta.ecommerce.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shweta.ecommerce.dto.UserDTO;
import com.shweta.ecommerce.entity.User;
import com.shweta.ecommerce.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // =========================
    // REGISTER USER
    // =========================
    public UserDTO registerUser(UserDTO userDTO) {

        // Check if email already exists
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Convert DTO to Entity
        User user = modelMapper.map(userDTO, User.class);

        // Encrypt password
        user.setPassword(
                passwordEncoder.encode(userDTO.getPassword())
        );

        // Save user
        User savedUser = userRepository.save(user);

        // Convert Entity to DTO
        UserDTO responseDTO = modelMapper.map(savedUser, UserDTO.class);

        // Don't return password
        responseDTO.setPassword(null);

        return responseDTO;
    }

    // =========================
    // LOGIN USER
    // =========================
public Map<String, String> loginUser(String email, String password) {

    // Find user by email
    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("Invalid email or password")
            );

    // Check password
    if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new RuntimeException("Invalid email or password");
    }

     // Generate access token
    String accessToken =
            jwtService.generateToken(user.getEmail());

    // Generate refresh token
    String refreshToken =
            jwtService.generateRefreshToken(user.getEmail());

    // Store both tokens
    Map<String, String> tokens = new HashMap<>();
    tokens.put("accessToken", accessToken);
    tokens.put("refreshToken", refreshToken);

    return tokens;
}


// =========================
// REFRESH ACCESS TOKEN
// =========================
public String refreshAccessToken(String refreshToken) {

    if (!jwtService.validateRefreshToken(refreshToken)) {
        throw new RuntimeException("Invalid or expired refresh token");
    }

    String email =
            jwtService.extractUsername(refreshToken);

    return jwtService.generateToken(email);
}
}
package com.shweta.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shweta.ecommerce.dto.LoginDTO;
import com.shweta.ecommerce.dto.ResponseDTO;
import com.shweta.ecommerce.dto.UserDTO;
import com.shweta.ecommerce.service.UserService;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.HashMap;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // =========================
    // REGISTER USER
    // =========================
    @PostMapping("/register")
    public ResponseDTO<UserDTO> register(
            @RequestBody @Valid UserDTO userDTO) {

        UserDTO registeredUser = userService.registerUser(userDTO);

        return new ResponseDTO<>(
                true,
                "User registered successfully",
                registeredUser
        );
    }

    // =========================
// LOGIN USER
// =========================
@PostMapping("/login")
public ResponseDTO<Map<String, String>> login(
        @RequestBody @Valid LoginDTO loginDTO) {

    Map<String, String> tokens = userService.loginUser(
            loginDTO.getEmail(),
            loginDTO.getPassword()
    );

    return new ResponseDTO<>(
            true,
            "Login successful",
            tokens
    );
}
// =========================
// REFRESH ACCESS TOKEN
// =========================
@PostMapping("/refresh")
public ResponseDTO<Map<String, String>> refresh(
        @RequestBody Map<String, String> request) {

    // Get refresh token from request
    String refreshToken = request.get("refreshToken");

    // Generate new access token
    String newAccessToken =
            userService.refreshAccessToken(refreshToken);

    // Prepare response
    Map<String, String> response = new HashMap<>();
    response.put("accessToken", newAccessToken);

    return new ResponseDTO<>(
            true,
            "Access token refreshed successfully",
            response
    );
}
}
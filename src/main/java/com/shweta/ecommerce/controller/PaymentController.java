package com.shweta.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.shweta.ecommerce.dto.PaymentRequestDTO;
import com.shweta.ecommerce.dto.PaymentResponseDTO;
import com.shweta.ecommerce.dto.ResponseDTO;
import com.shweta.ecommerce.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Create Payment
    @PostMapping
    public ResponseEntity<ResponseDTO<PaymentResponseDTO>> createPayment(
            @Valid @RequestBody PaymentRequestDTO request,
            Authentication authentication) {

        String email = authentication.getName();

        PaymentResponseDTO payment =
                paymentService.createPayment(request, email);

        ResponseDTO<PaymentResponseDTO> response =
                new ResponseDTO<>(
                        true,
                        "Payment successful",
                        payment
                );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get Payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<PaymentResponseDTO>> getPaymentById(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        PaymentResponseDTO payment =
                paymentService.getPaymentById(id, email);

        ResponseDTO<PaymentResponseDTO> response =
                new ResponseDTO<>(
                        true,
                        "Payment fetched successfully",
                        payment
                );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
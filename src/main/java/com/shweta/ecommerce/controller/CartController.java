package com.shweta.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.shweta.ecommerce.dto.CartDTO;
import com.shweta.ecommerce.dto.CartResponseDTO;
import com.shweta.ecommerce.dto.ResponseDTO;
import com.shweta.ecommerce.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<ResponseDTO<CartResponseDTO>> addToCart(
            @Valid @RequestBody CartDTO request,
            Authentication authentication) {

        String email = authentication.getName();

        CartResponseDTO cart = cartService.addToCart(request, email);

        ResponseDTO<CartResponseDTO> response = new ResponseDTO<>(
                true,
                "Product added to cart successfully",
                cart);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<CartResponseDTO>>> getMyCart(
            Authentication authentication) {

        String email = authentication.getName();

        List<CartResponseDTO> cart = cartService.getMyCart(email);

        ResponseDTO<List<CartResponseDTO>> response = new ResponseDTO<>(
                true,
                "Cart fetched successfully",
                cart);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<CartResponseDTO>> updateCart(
            @PathVariable Long id,
            @Valid @RequestBody CartDTO request,
            Authentication authentication) {

        String email = authentication.getName();

        CartResponseDTO cart = cartService.updateCart(id, request, email);

        ResponseDTO<CartResponseDTO> response = new ResponseDTO<>(
                true,
                "Cart updated successfully",
                cart);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> removeFromCart(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        cartService.removeFromCart(id, email);

        ResponseDTO<Void> response = new ResponseDTO<>(
                true,
                "Product removed from cart successfully",
                null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO<Void>> clearCart(
            Authentication authentication) {

        String email = authentication.getName();

        cartService.clearCart(email);

        ResponseDTO<Void> response = new ResponseDTO<>(
                true,
                "Cart cleared successfully",
                null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
package com.shweta.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.shweta.ecommerce.dto.ResponseDTO;
import com.shweta.ecommerce.dto.WishlistResponseDTO;
import com.shweta.ecommerce.service.WishlistService;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/{productId}")
    public ResponseEntity<ResponseDTO<WishlistResponseDTO>> addToWishlist(
            @PathVariable Long productId,
            Authentication authentication) {

        String email = authentication.getName();

        WishlistResponseDTO wishlist =
                wishlistService.addToWishlist(productId, email);

        ResponseDTO<WishlistResponseDTO> response = new ResponseDTO<>(
                true,
                "Product added to wishlist successfully",
                wishlist);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<WishlistResponseDTO>>> getMyWishlist(
            Authentication authentication) {

        String email = authentication.getName();

        List<WishlistResponseDTO> wishlist =
                wishlistService.getMyWishlist(email);

        ResponseDTO<List<WishlistResponseDTO>> response = new ResponseDTO<>(
                true,
                "Wishlist fetched successfully",
                wishlist);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> removeFromWishlist(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        wishlistService.removeFromWishlist(id, email);

        ResponseDTO<Void> response = new ResponseDTO<>(
                true,
                "Product removed from wishlist successfully",
                null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO<Void>> clearWishlist(
            Authentication authentication) {

        String email = authentication.getName();

        wishlistService.clearWishlist(email);

        ResponseDTO<Void> response = new ResponseDTO<>(
                true,
                "Wishlist cleared successfully",
                null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
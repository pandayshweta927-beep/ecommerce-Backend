package com.shweta.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.shweta.ecommerce.dto.ResponseDTO;
import com.shweta.ecommerce.dto.ReviewDTO;
import com.shweta.ecommerce.dto.ReviewResponseDTO;
import com.shweta.ecommerce.service.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Add Review
    @PostMapping
    public ResponseEntity<ResponseDTO<ReviewResponseDTO>> addReview(
            @Valid @RequestBody ReviewDTO reviewDTO,
            Authentication authentication) {

        String email = authentication.getName();

        ReviewResponseDTO review =
                reviewService.addReview(reviewDTO, email);

        ResponseDTO<ReviewResponseDTO> response =
                new ResponseDTO<>(
                        true,
                        "Review added successfully",
                        review
                );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get Reviews For Product
    @GetMapping("/product/{productId}")
    public ResponseEntity<ResponseDTO<List<ReviewResponseDTO>>> getProductReviews(
            @PathVariable Long productId) {

        List<ReviewResponseDTO> reviews =
                reviewService.getProductReviews(productId);

        ResponseDTO<List<ReviewResponseDTO>> response =
                new ResponseDTO<>(
                        true,
                        "Product reviews fetched successfully",
                        reviews
                );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get My Reviews
    @GetMapping("/my")
    public ResponseEntity<ResponseDTO<List<ReviewResponseDTO>>> getMyReviews(
            Authentication authentication) {

        String email = authentication.getName();

        List<ReviewResponseDTO> reviews =
                reviewService.getMyReviews(email);

        ResponseDTO<List<ReviewResponseDTO>> response =
                new ResponseDTO<>(
                        true,
                        "Your reviews fetched successfully",
                        reviews
                );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Delete Review
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteReview(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        reviewService.deleteReview(id, email);

        ResponseDTO<Void> response =
                new ResponseDTO<>(
                        true,
                        "Review deleted successfully",
                        null
                );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

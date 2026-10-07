package com.shweta.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shweta.ecommerce.dto.ReviewDTO;
import com.shweta.ecommerce.dto.ReviewResponseDTO;
import com.shweta.ecommerce.entity.Product;
import com.shweta.ecommerce.entity.Review;
import com.shweta.ecommerce.entity.User;
import com.shweta.ecommerce.repository.ProductRepository;
import com.shweta.ecommerce.repository.ReviewRepository;
import com.shweta.ecommerce.repository.UserRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Add Review
    public ReviewResponseDTO addReview(ReviewDTO reviewDTO, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        if (reviewRepository.findByUserAndProduct(user, product).isPresent()) {
            throw new RuntimeException("You have already reviewed this product");
        }

        Review review = new Review();

        review.setUser(user);
        review.setProduct(product);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        Review savedReview = reviewRepository.save(review);

        return convertToResponseDTO(savedReview);
    }

    // Get Reviews For Product
    public List<ReviewResponseDTO> getProductReviews(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        return reviewRepository.findByProduct(product)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // Get My Reviews
    public List<ReviewResponseDTO> getMyReviews(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return reviewRepository.findByUser(user)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // Delete Review
    public void deleteReview(Long id, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Review review = reviewRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to delete this review");
        }

        reviewRepository.delete(review);
    }

    // Convert Entity to Response DTO
    private ReviewResponseDTO convertToResponseDTO(Review review) {

        return new ReviewResponseDTO(
                review.getId(),
                review.getProduct().getId(),
                review.getProduct().getName(),
                review.getUser().getName(),
                review.getRating(),
                review.getComment()
        );
    }
}

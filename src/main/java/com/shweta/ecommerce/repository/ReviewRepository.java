package com.shweta.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shweta.ecommerce.entity.Product;
import com.shweta.ecommerce.entity.Review;
import com.shweta.ecommerce.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct(Product product);

    List<Review> findByUser(User user);

    Optional<Review> findByUserAndProduct(User user, Product product);
}

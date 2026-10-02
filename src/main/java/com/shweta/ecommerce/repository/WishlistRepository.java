package com.shweta.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shweta.ecommerce.entity.Product;
import com.shweta.ecommerce.entity.User;
import com.shweta.ecommerce.entity.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUser(User user);

    Optional<Wishlist> findByUserAndProduct(User user, Product product);
}
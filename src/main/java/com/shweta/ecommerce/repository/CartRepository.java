package com.shweta.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shweta.ecommerce.entity.Cart;
import com.shweta.ecommerce.entity.Product;
import com.shweta.ecommerce.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUser(User user);

    Optional<Cart> findByUserAndProduct(User user, Product product);

}
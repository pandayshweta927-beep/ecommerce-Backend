package com.shweta.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shweta.ecommerce.dto.WishlistResponseDTO;
import com.shweta.ecommerce.entity.Product;
import com.shweta.ecommerce.entity.User;
import com.shweta.ecommerce.entity.Wishlist;
import com.shweta.ecommerce.repository.ProductRepository;
import com.shweta.ecommerce.repository.UserRepository;
import com.shweta.ecommerce.repository.WishlistRepository;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public WishlistResponseDTO addToWishlist(Long productId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist existingWishlist =
                wishlistRepository.findByUserAndProduct(user, product)
                        .orElse(null);

        if (existingWishlist != null) {
            throw new RuntimeException("Product already exists in wishlist");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);

        return convertToResponseDTO(savedWishlist);
    }

    public List<WishlistResponseDTO> getMyWishlist(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return wishlistRepository.findByUser(user)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public void removeFromWishlist(Long id, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wishlist wishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found"));

        if (!wishlist.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "Wishlist item not found or you are not authorized to access it");
        }

        wishlistRepository.delete(wishlist);
    }

    public void clearWishlist(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Wishlist> wishlistItems = wishlistRepository.findByUser(user);

        wishlistRepository.deleteAll(wishlistItems);
    }

    private WishlistResponseDTO convertToResponseDTO(Wishlist wishlist) {

        Product product = wishlist.getProduct();

        return new WishlistResponseDTO(
                wishlist.getId(),
                product.getId(),
                product.getName(),
                product.getPrice());
    }
}

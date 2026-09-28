package com.shweta.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shweta.ecommerce.dto.CartDTO;
import com.shweta.ecommerce.dto.CartResponseDTO;
import com.shweta.ecommerce.entity.Cart;
import com.shweta.ecommerce.entity.Product;
import com.shweta.ecommerce.entity.User;
import com.shweta.ecommerce.repository.CartRepository;
import com.shweta.ecommerce.repository.ProductRepository;
import com.shweta.ecommerce.repository.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public CartResponseDTO addToCart(CartDTO dto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < dto.getQuantity()) {
            throw new RuntimeException("Insufficient product stock");
        }

        Cart cart = cartRepository.findByUserAndProduct(user, product)
                .orElse(null);

        if (cart != null) {

            int newQuantity = cart.getQuantity() + dto.getQuantity();

            if (product.getQuantity() < newQuantity) {
                throw new RuntimeException("Insufficient product stock");
            }

            cart.setQuantity(newQuantity);

        } else {

            cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(dto.getQuantity());
        }

        Cart savedCart = cartRepository.save(cart);

        return convertToResponseDTO(savedCart);
    }

    public List<CartResponseDTO> getMyCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public CartResponseDTO updateCart(Long id, CartDTO dto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cart.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "Cart item not found or you are not authorized to access it");
        }

        Product product = cart.getProduct();

        if (product.getQuantity() < dto.getQuantity()) {
            throw new RuntimeException("Insufficient product stock");
        }

        cart.setQuantity(dto.getQuantity());

        Cart updatedCart = cartRepository.save(cart);

        return convertToResponseDTO(updatedCart);
    }

    public void removeFromCart(Long id, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cart.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "Cart item not found or you are not authorized to access it");
        }

        cartRepository.delete(cart);
    }

    public void clearCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> cartItems = cartRepository.findByUser(user);

        cartRepository.deleteAll(cartItems);
    }

    private CartResponseDTO convertToResponseDTO(Cart cart) {

        Product product = cart.getProduct();

        Double totalPrice = product.getPrice() * cart.getQuantity();

        return new CartResponseDTO(
                cart.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                cart.getQuantity(),
                totalPrice);
    }
}

package com.shweta.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shweta.ecommerce.dto.ProductDTO;
import com.shweta.ecommerce.dto.ResponseDTO;
import com.shweta.ecommerce.entity.Product;
import com.shweta.ecommerce.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

@Autowired
private ProductService productService;

// Add Product
@PostMapping
public ResponseDTO<Product> addProduct(
        @RequestBody @Valid ProductDTO productDTO) {

    Product product = productService.saveProduct(productDTO);

    return new ResponseDTO<>(
            true,
            "Product Added Successfully",
            product
    );
}

// Get All Products
@GetMapping
public ResponseDTO<List<Product>> getAllProducts() {

    List<Product> products = productService.getAllProducts();

    return new ResponseDTO<>(
            true,
            "Products fetched successfully",
            products
    );
}

// Get Product By ID
@GetMapping("/{id}")
public ResponseDTO<Product> getProductById(
        @PathVariable Long id) {

    Product product = productService.getProductById(id);

    return new ResponseDTO<>(
            true,
            "Product found successfully",
            product
    );
}

// Update Product
@PutMapping("/{id}")
public ResponseDTO<Product> updateProduct(
        @PathVariable Long id,
        @RequestBody @Valid ProductDTO productDTO) {

    Product product = productService.updateProduct(id, productDTO);

    return new ResponseDTO<>(
            true,
            "Product updated successfully",
            product
    );
}

// Delete Product
@DeleteMapping("/{id}")
public ResponseDTO<Void> deleteProduct(@PathVariable Long id) {

    productService.deleteProduct(id);

    return new ResponseDTO<>(
            true,
            "Product deleted successfully",
            null
    );
}


}

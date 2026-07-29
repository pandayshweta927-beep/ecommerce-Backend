package com.shweta.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.shweta.ecommerce.entity.Product;
import com.shweta.ecommerce.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Add Product
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // Get All Products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get Product By ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    // Update Product
@PutMapping("/{id}")
public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {

    return productService.updateProduct(id, product);
}
// Delete Product
@DeleteMapping("/{id}")
public String deleteProduct(@PathVariable Long id) {

    productService.deleteProduct(id);

    return "Product Deleted Successfully";
}
}
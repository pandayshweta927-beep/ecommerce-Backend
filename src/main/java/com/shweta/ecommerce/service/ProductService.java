package com.shweta.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shweta.ecommerce.dto.ProductDTO;
import com.shweta.ecommerce.entity.Product;
import com.shweta.ecommerce.exception.ProductNotFoundException;
import com.shweta.ecommerce.repository.ProductRepository;
import com.shweta.ecommerce.exception.ProductNotFoundException;
import org.modelmapper.ModelMapper;
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
     private ModelMapper modelMapper;
    // Add Product

public Product saveProduct(ProductDTO productDTO) {

    Product product = modelMapper.map(productDTO, Product.class);

    return productRepository.save(product);
}

    // Get All Products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get Product By ID
    public Product getProductById(Long id) {

    return productRepository.findById(id)
            .orElseThrow(() ->
                    new ProductNotFoundException(
                            "Product with ID " + id + " not found"
                    )
            );
}

   // Update Product
public Product updateProduct(Long id, ProductDTO productDTO) {

    Product existingProduct = productRepository.findById(id)
            .orElseThrow(() ->
                    new ProductNotFoundException(
                            "Product with ID " + id + " not found"
                    )
            );

    modelMapper.map(productDTO, existingProduct);

    return productRepository.save(existingProduct);
}
    // Delete Product
    public String deleteProduct(Long id) {

        if (productRepository.existsById(id)) {

            productRepository.deleteById(id);
            return "Product Deleted Successfully";
        }

        return "Product Not Found";
    }
}
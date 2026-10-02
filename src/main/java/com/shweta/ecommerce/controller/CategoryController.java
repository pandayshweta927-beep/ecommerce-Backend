package com.shweta.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shweta.ecommerce.dto.CategoryDTO;
import com.shweta.ecommerce.dto.CategoryResponseDTO;
import com.shweta.ecommerce.dto.ResponseDTO;
import com.shweta.ecommerce.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ResponseDTO<CategoryResponseDTO>> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO) {

        CategoryResponseDTO category =
                categoryService.createCategory(categoryDTO);

        ResponseDTO<CategoryResponseDTO> response = new ResponseDTO<>(
                true,
                "Category created successfully",
                category
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<CategoryResponseDTO>>> getAllCategories() {

        List<CategoryResponseDTO> categories =
                categoryService.getAllCategories();

        ResponseDTO<List<CategoryResponseDTO>> response = new ResponseDTO<>(
                true,
                "Categories fetched successfully",
                categories
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CategoryResponseDTO>> getCategoryById(
            @PathVariable Long id) {

        CategoryResponseDTO category =
                categoryService.getCategoryById(id);

        ResponseDTO<CategoryResponseDTO> response = new ResponseDTO<>(
                true,
                "Category fetched successfully",
                category
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<CategoryResponseDTO>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO) {

        CategoryResponseDTO category =
                categoryService.updateCategory(id, categoryDTO);

        ResponseDTO<CategoryResponseDTO> response = new ResponseDTO<>(
                true,
                "Category updated successfully",
                category
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteCategory(
            @PathVariable Long id) {

        categoryService.deleteCategory(id);

        ResponseDTO<Void> response = new ResponseDTO<>(
                true,
                "Category deleted successfully",
                null
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
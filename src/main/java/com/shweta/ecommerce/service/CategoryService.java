package com.shweta.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shweta.ecommerce.dto.CategoryDTO;
import com.shweta.ecommerce.dto.CategoryResponseDTO;
import com.shweta.ecommerce.entity.Category;
import com.shweta.ecommerce.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryResponseDTO createCategory(CategoryDTO categoryDTO) {

        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new RuntimeException("Category already exists");
        }

        Category category = new Category();
        category.setName(categoryDTO.getName());

        Category savedCategory = categoryRepository.save(category);

        return convertToResponseDTO(savedCategory);
    }

    public List<CategoryResponseDTO> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public CategoryResponseDTO getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return convertToResponseDTO(category);
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryDTO categoryDTO) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (categoryRepository.existsByName(categoryDTO.getName())
                && !category.getName().equals(categoryDTO.getName())) {
            throw new RuntimeException("Category already exists");
        }

        category.setName(categoryDTO.getName());

        Category updatedCategory = categoryRepository.save(category);

        return convertToResponseDTO(updatedCategory);
    }

    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        categoryRepository.delete(category);
    }

    private CategoryResponseDTO convertToResponseDTO(Category category) {

        return new CategoryResponseDTO(
                category.getId(),
                category.getName()
        );
    }
}
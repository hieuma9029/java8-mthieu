package org.example.shopping.service.impl;

import org.example.shopping.entity.Category;
import org.example.shopping.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void saveShouldMarkCategoryAsActiveAndSetCreatedAt() {
        Category category = new Category();
        category.setName("Điện thoại");

        categoryService.save(category);

        assertFalse(category.getIsDelete());
        assertNotNull(category.getCreatedAt());
        verify(categoryRepository).save(category);
    }
}

package org.example.shopping.service.impl;

import org.example.shopping.entity.Category;
import org.example.shopping.repository.CategoryRepository;
import org.example.shopping.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Hiện thực nghiệp vụ cho danh mục sản phẩm bằng {@link CategoryRepository}.
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category, Integer, CategoryRepository> implements CategoryService {

    /**
     * Khởi tạo service với repository truy cập dữ liệu danh mục.
     *
     * @param categoryRepository repository dùng cho các thao tác danh mục
     */
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        super(categoryRepository);
    }

    @Override
    public List<Category> findAll() {
        return repository.findByIsDeleteFalse();
    }

    @Override
    public Page<Category> findAll(int page, int size) {
        return repository.findByIsDeleteFalse(PageRequest.of(page, size));
    }

    @Override
    public Category findById(Integer id) {
        Category category = repository.findById(id).orElse(null);
        if (category == null || Boolean.TRUE.equals(category.getIsDelete())) {
            return null;
        }
        return category;
    }

    @Override
    public void save(Category category) {
        category.setIsDelete(false);
        category.setCreatedAt(LocalDateTime.now());
        repository.save(category);
    }

    @Override
    public void update(Integer id, Category category) {
        Category existingCategory = findById(id);
        if (existingCategory != null) {
            existingCategory.setName(category.getName());
            existingCategory.setUpdatedAt(LocalDateTime.now());
            repository.save(existingCategory);
        }
    }

    @Override
    public void delete(Integer id) {
        Category category = findById(id);
        if (category != null) {
            category.setIsDelete(true);
            category.setDeletedAt(LocalDateTime.now());
            repository.save(category);
        }
    }

    @Override
    public void restore(Integer id) {
        Category category = repository.findById(id).orElse(null);
        if (category != null && Boolean.TRUE.equals(category.getIsDelete())) {
            category.setIsDelete(false);
            category.setDeletedAt(null);
            category.setUpdatedAt(LocalDateTime.now());
            repository.save(category);
        }
    }

    @Override
    public boolean existsByName(String name) {
        return repository.findByIsDeleteFalse().stream()
                .map(Category::getName)
                .anyMatch(name::equals);
    }

    @Override
    protected void copyForUpdate(Category existing, Category source) {
        existing.setName(source.getName());
    }
}

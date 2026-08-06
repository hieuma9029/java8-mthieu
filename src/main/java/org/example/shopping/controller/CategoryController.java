package org.example.shopping.controller;

import org.example.shopping.entity.Category;
import org.example.shopping.model.CategoryRequest;
import org.example.shopping.model.CategoryResponse;
import org.example.shopping.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.example.shopping.model.PaginationResult;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * API REST quản lý danh mục sản phẩm tại đường dẫn /categories.
 */
@RestController
@RequestMapping("/categories")
/** Controller REST xử lý các thao tác quản lý danh mục sản phẩm. */
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    /** Lấy toàn bộ danh mục hiện có để hiển thị cho frontend. */
    public List<CategoryResponse> getAllCategories() {
        return categoryService.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/page")
    /** Lấy danh mục theo trang để tránh tải quá nhiều dữ liệu cùng lúc. */
    public PaginationResult<CategoryResponse> getCategoriesByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Category> categoryPage = categoryService.findAll(page, size);

        PaginationResult<CategoryResponse> result = new PaginationResult<>();
        result.setList(categoryPage.getContent().stream().map(this::toResponse).collect(Collectors.toList()));
        result.setCurrentPage(categoryPage.getNumber());
        result.setTotalPages(categoryPage.getTotalPages());
        result.setTotalItems(categoryPage.getTotalElements());
        result.setHasNext(categoryPage.hasNext());
        result.setHasPrevious(categoryPage.hasPrevious());
        return result;
    }

    @GetMapping("/{id}")
    /** Lấy thông tin chi tiết một danh mục theo mã định danh. */
    public CategoryResponse getCategoryById(@PathVariable Integer id) {
        Category category = categoryService.findById(id);
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy danh mục");
        }
        return toResponse(category);
    }

    @PostMapping
    /** Tạo mới một danh mục sau khi kiểm tra tên danh mục chưa bị trùng. */
    public void saveCategory(@Valid @RequestBody CategoryRequest request) {
        if (categoryService.existsByName(request.getName())) {
            throw new RuntimeException("Tên danh mục đã tồn tại.");
        }

        Category category = new Category();
        category.setName(request.getName());
        categoryService.save(category);
    }

    @PutMapping("/{id}")
    /** Cập nhật tên danh mục hiện có và kiểm tra tính duy nhất trước khi lưu. */
    public void updateCategory(@PathVariable Integer id, @Valid @RequestBody CategoryRequest request) {
        Category existingCategory = categoryService.findById(id);
        if (existingCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy danh mục");
        }

        if (!existingCategory.getName().equals(request.getName()) && categoryService.existsByName(request.getName())) {
            throw new RuntimeException("Tên danh mục đã tồn tại.");
        }

        Category category = new Category();
        category.setName(request.getName());
        categoryService.update(id, category);
    }

    @DeleteMapping("/{id}")
    /** Xóa mềm danh mục để dữ liệu vẫn còn lưu trong hệ thống nhưng không hiển thị nữa. */
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.delete(id);
    }

    @PutMapping("/{id}/restore")
    /** Khôi phục danh mục đã bị xóa mềm trước đó. */
    public void restoreCategory(@PathVariable Integer id) {
        categoryService.restore(id);
    }

    /** Chuyển entity danh mục sang DTO dùng cho API response. */
    private CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }
}

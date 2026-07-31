package org.example.shopping.model;

import javax.validation.constraints.NotBlank;

/**
 * DTO dùng để nhận dữ liệu danh mục từ client khi tạo hoặc cập nhật.
 */
public class CategoryRequest {

    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

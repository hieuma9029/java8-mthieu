package org.example.shopping.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DTO dùng để nhận dữ liệu từ client khi thêm hoặc sửa sản phẩm.
 * Chứa các trường cần thiết để tạo mới hoặc cập nhật sản phẩm.
 */
public class ProductRequest {
    private String code;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    private BigDecimal price;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải là số không âm")
    private Integer quantity;

    /**
     * Các getter/setter bên dưới lần lượt đọc hoặc gán code, name và price
     * của yêu cầu tạo/cập nhật sản phẩm. Tham số của mỗi setter là giá trị mới
     * cho thuộc tính có cùng tên.
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

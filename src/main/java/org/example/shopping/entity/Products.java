package org.example.shopping.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(columnNames = "code"),
        @UniqueConstraint(columnNames = "name")
})
/**
 * Entity ánh xạ bảng products, lưu thông tin sản phẩm trong hệ thống bán hàng.
 * Mỗi sản phẩm có mã, tên, giá, số lượng tồn kho và trạng thái xóa mềm để quản lý dữ liệu an toàn.
 */
public class Products extends BaseEntity {

    /** Khóa chính tự tăng của sản phẩm. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Mã sản phẩm dùng để nhận diện và phân biệt với các sản phẩm khác. */
    @NotBlank(message = "Code không được để trống")
    @Column(length = 20, nullable = false)
    private String code;

    /** Ảnh đại diện của sản phẩm. */
    @Lob
    private byte[] image;

    /** Tên sản phẩm, cần duy nhất trong hệ thống. */
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Column(nullable = false, unique = true)
    private String name;

    /** Giá bán của sản phẩm, phải lớn hơn 0. */
    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    /** Số lượng tồn kho hiện có của sản phẩm. */
    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải là số không âm")
    @Column(nullable = false)
    private Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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

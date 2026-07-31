package org.example.shopping.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * Entity ánh xạ bảng categories, lưu thông tin danh mục sản phẩm.
 */
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    /** Khóa chính tự tăng của danh mục. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Tên danh mục, cần duy nhất để dễ quản lý và tra cứu. */
    @NotBlank(message = "Tên danh mục không được để trống")
    @Column(nullable = false, unique = true)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

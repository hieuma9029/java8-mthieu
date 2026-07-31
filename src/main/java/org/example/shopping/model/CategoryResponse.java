package org.example.shopping.model;

/**
 * DTO dùng để trả thông tin danh mục cho client.
 */
public class CategoryResponse {
    private Integer id;
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

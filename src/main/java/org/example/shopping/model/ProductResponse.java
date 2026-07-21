package org.example.shopping.model;

/**
 * DTO dùng để trả dữ liệu sản phẩm cho client.
 */
public class ProductResponse {

    private Integer id;

    private String code;

    private String name;

    private Double price;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
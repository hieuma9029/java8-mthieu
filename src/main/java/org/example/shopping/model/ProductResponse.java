package org.example.shopping.model;
import java.math.BigDecimal;
/**
 * DTO dùng để trả dữ liệu sản phẩm cho client.
 */
public class ProductResponse {
    private Integer id;

    private String code;

    private String name;

    private BigDecimal price;

    /**
     * Các getter/setter bên dưới lần lượt đọc hoặc gán id, code, name và price
     * của DTO phản hồi. Tham số của mỗi setter là giá trị mới cho thuộc tính có
     * cùng tên.
     */
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

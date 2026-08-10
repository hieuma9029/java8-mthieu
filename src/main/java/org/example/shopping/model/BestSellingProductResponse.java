package org.example.shopping.model;

/**
 * DTO trả về thông tin sản phẩm bán chạy nhất.
 */
public class BestSellingProductResponse {
    private Integer productId;
    private String productName;
    private Long quantitySold;
    private java.math.BigDecimal revenue;

    public BestSellingProductResponse() {
    }

    public BestSellingProductResponse(Integer productId, String productName, Long quantitySold, java.math.BigDecimal revenue) {
        this.productId = productId;
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.revenue = revenue;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Long quantitySold) {
        this.quantitySold = quantitySold;
    }

    public java.math.BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(java.math.BigDecimal revenue) {
        this.revenue = revenue;
    }
}

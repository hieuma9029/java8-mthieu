package org.example.shopping.order.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO trả về số liệu thống kê đơn hàng dành cho trang admin.
 */
public class AdminOrderStatsResponse {
    private BigDecimal totalRevenue;
    private Long totalOrders;
    private List<BestSellingProductResponse> topProducts;

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public List<BestSellingProductResponse> getTopProducts() {
        return topProducts;
    }

    public void setTopProducts(List<BestSellingProductResponse> topProducts) {
        this.topProducts = topProducts;
    }
}

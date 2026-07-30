package org.example.shopping.entity;

/**
 * Enum đại diện cho các trạng thái đơn hàng có thể tồn tại trong hệ thống.
 * Các giá trị này được dùng để hiển thị trạng thái cho frontend và lưu vào database.
 */
public enum OrderStatus {
    /** Đơn hàng vừa tạo nhưng chưa được xác nhận. */
    PENDING("Chưa xác nhận đơn"),
    /** Đơn hàng đã được xác nhận bởi quản trị viên hoặc hệ thống. */
    CONFIRMED("Đã xác nhận"),
    /** Đơn hàng đã bị hủy. */
    CANCELLED("Đã hủy"),
    /** Đơn hàng đã được giao cho khách. */
    SHIPPED("Đã giao"),
    /** Khách hàng đã nhận hàng thành công. */
    DELIVERED("Đã nhận"),
    /** Đơn hàng đã được trả lại và cần hoàn lại tồn kho. */
    RETURNED("Đã trả hàng");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    /**
     * Trả về nhãn hiển thị thân thiện với người dùng.
     *
     * @return chuỗi mô tả trạng thái
     */
    public String getLabel() {
        return label;
    }
}

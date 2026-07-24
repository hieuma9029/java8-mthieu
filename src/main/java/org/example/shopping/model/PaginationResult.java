package org.example.shopping.model;

import java.util.List;

/**
 * Đóng gói dữ liệu phân trang trả về cho client.
 *
 * @param <T> kiểu dữ liệu của từng phần tử trong danh sách
 */
public class PaginationResult<T> {

    /** Danh sách dữ liệu của trang hiện tại. */
    private List<T> list;

    /** Trang hiện tại (bắt đầu từ 0). */
    private int currentPage;

    /** Tổng số trang. */
    private int totalPages;

    /** Tổng số phần tử. */
    private long totalItems;

    /** Có trang tiếp theo hay không. */
    private boolean hasNext;

    /** Có trang trước hay không. */
    private boolean hasPrevious;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}
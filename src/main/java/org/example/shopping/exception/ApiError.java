package org.example.shopping.exception;

import java.time.LocalDateTime;

/**
 * DTO dùng để trả thông tin lỗi cho client.
 */
public class ApiError {

    /** Thời điểm xảy ra lỗi. */
    private LocalDateTime timestamp;

    /** Mã trạng thái HTTP. */
    private int status;

    /** Nội dung lỗi. */
    private String message;

    public ApiError() {
    }

    public ApiError(LocalDateTime timestamp, int status, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
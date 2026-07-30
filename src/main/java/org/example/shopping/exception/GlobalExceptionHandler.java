package org.example.shopping.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.dao.DataIntegrityViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Xử lý tập trung các Exception của toàn bộ RestController.
 */
@RestControllerAdvice
/**
 * Xử lý lỗi tập trung cho toàn bộ controller để trả về response thống nhất.
 * Lớp này chuyển các exception phát sinh từ validation, business logic hoặc database
 * thành payload lỗi có cấu trúc rõ ràng cho frontend.
 */
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Chuyển lỗi ràng buộc database, chẳng hạn giá trị unique bị trùng, thành HTTP 409.
     *
     * @param ex lỗi do tầng truy cập dữ liệu phát sinh
     * @return response lỗi xung đột dữ liệu
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ApiError error = new ApiError();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setMessage("Duplicate or invalid data constraint.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Xử lý lỗi do tham số hoặc trạng thái không hợp lệ.
     *
     * @param ex Exception xảy ra
     * @return Response chứa thông tin lỗi
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ApiError> handleBadRequestException(RuntimeException ex) {

        ApiError error = new ApiError();

        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * Xử lý các lỗi không mong muốn và trả về HTTP 500.
     *
     * @param ex Exception xảy ra
     * @return Response chứa thông tin lỗi máy chủ
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleServerException(Exception ex) {
        logger.error("Unhandled exception in controller", ex);
        ApiError error = new ApiError();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        // Trả thông điệp lỗi thực tế để hỗ trợ debug cục bộ.
        String msg = ex.getMessage();
        if (msg == null || msg.isEmpty()) {
            msg = ex.toString();
        }
        error.setMessage(msg);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    /**
     * Xử lý lỗi khi @Valid kiểm tra dữ liệu không hợp lệ.
     *
     * @param ex Exception do @Valid sinh ra
     * @return Response chứa thông tin lỗi
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex) {

        FieldError fieldError = ex.getBindingResult().getFieldError();

        ApiError error = new ApiError();

        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());

        if (fieldError != null) {
            error.setMessage(fieldError.getDefaultMessage());
        } else {
            error.setMessage("Dữ liệu không hợp lệ.");
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}

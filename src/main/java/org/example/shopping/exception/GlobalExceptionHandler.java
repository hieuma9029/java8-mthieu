package org.example.shopping.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildErrorBody("Duplicate or invalid data constraint.", HttpStatus.CONFLICT.value()));
    }

    /**
     * Xử lý các ResponseStatusException do controller/service ném ra;
     * trả về body chỉ chứa message theo contract mới.
     *
     * @param ex exception có HTTP status và reason message
     * @return response với status tương ứng và payload message-only
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        String message = ex.getReason();
        if (message == null || message.trim().isEmpty()) {
            message = "Yêu cầu không hợp lệ.";
        }

        HttpStatus status = ex.getStatus();
        if (status == null) {
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity
                .status(status)
                .body(buildErrorBody(message, status.value()));
    }

    /**
     * Xử lý lỗi do tham số hoặc trạng thái không hợp lệ.
     *
     * @param ex Exception xảy ra
     * @return Response chứa thông tin lỗi
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequestException(RuntimeException ex) {
        String exMsg = ex.getMessage();
        String msg = exMsg == null || exMsg.trim().isEmpty()
                ? "Yêu cầu không hợp lệ."
                : exMsg;

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildErrorBody(msg, HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Xử lý các lỗi không mong muốn và trả về HTTP 500.
     *
     * @param ex Exception xảy ra
     * @return Response chứa thông tin lỗi máy chủ
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleServerException(Exception ex) {
        logger.error("Unhandled exception in controller", ex);
        String msg = ex.getMessage();
        if (msg == null || msg.trim().isEmpty()) {
            msg = ex.toString();
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorBody(msg, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    /**
     * Xử lý lỗi khi @Valid kiểm tra dữ liệu không hợp lệ.
     *
     * @param ex Exception do @Valid sinh ra
     * @return Response chứa thông tin lỗi
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex) {

        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = (fieldError != null && fieldError.getDefaultMessage() != null)
                ? fieldError.getDefaultMessage()
                : "Dữ liệu không hợp lệ.";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildErrorBody(message, HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Trả về payload lỗi thống nhất. Với user không thêm code, với admin thêm code.
     *
     * @param message nội dung hiển thị cho người dùng
     * @param statusCode mã HTTP/status tương ứng
     * @return map body dạng message-only hoặc message + code
     */
    private Map<String, Object> buildErrorBody(String message, int statusCode) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);

        if (isAdmin()) {
            body.put("code", statusCode);
        }

        return body;
    }

    /**
     * Kiểm tra xem request hiện tại thuộc admin hay user.
     *
     * @return true nếu SecurityContext đang chứa vai trò ADMIN
     */
    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority()) || "ADMIN".equals(authority.getAuthority())) {
                return true;
            }
        }

        return false;
    }
}

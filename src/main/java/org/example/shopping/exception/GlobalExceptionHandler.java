    package org.example.shopping.exception;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.bind.annotation.RestControllerAdvice;
    import org.springframework.web.bind.MethodArgumentNotValidException;
    import org.springframework.validation.FieldError;

    import java.time.LocalDateTime;

    /**
     * Xử lý tập trung các Exception của toàn bộ RestController.
     */
    @RestControllerAdvice
    public class GlobalExceptionHandler {

        /**
         * Xử lý RuntimeException.
         *
         * @param ex Exception xảy ra
         * @return Response chứa thông tin lỗi
         */
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex) {

            ApiError error = new ApiError();

            error.setTimestamp(LocalDateTime.now());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            error.setMessage(ex.getMessage());

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
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
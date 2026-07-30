package org.example.shopping.model;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartItemRequestValidationTest {

    private final Validator validator;

    CartItemRequestValidationTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void shouldAllowZeroQuantity() {
        CartItemRequest request = new CartItemRequest();
        request.setProductId(1);
        request.setQuantity(0);

        Set<ConstraintViolation<CartItemRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "Quantity 0 should be accepted");
    }

    @Test
    void shouldRejectNegativeQuantity() {
        CartItemRequest request = new CartItemRequest();
        request.setProductId(1);
        request.setQuantity(-1);

        Set<ConstraintViolation<CartItemRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Negative quantity should be rejected");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("lớn hơn hoặc bằng 0")),
                "Violation should explain quantity must be >= 0");
    }
}

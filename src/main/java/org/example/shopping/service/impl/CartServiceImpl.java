package org.example.shopping.service.impl;

import org.example.shopping.entity.Accounts;
import org.example.shopping.entity.CartItems;
import org.example.shopping.entity.Carts;
import org.example.shopping.entity.Products;
import org.example.shopping.model.CartItemRequest;
import org.example.shopping.model.CartItemResponse;
import org.example.shopping.model.CartResponse;
import org.example.shopping.repository.AccountRepository;
import org.example.shopping.repository.CartItemRepository;
import org.example.shopping.repository.CartRepository;
import org.example.shopping.repository.ProductRepository;
import org.example.shopping.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
/** Hiện thực nghiệp vụ giỏ hàng, gắn giỏ hàng với tài khoản trong Spring Security. */
public class CartServiceImpl implements CartService {
    /** Repository lưu giỏ hàng của từng tài khoản. */
    private final CartRepository cartRepository;
    /** Repository lưu từng dòng sản phẩm trong giỏ. */
    private final CartItemRepository cartItemRepository;
    /** Repository dùng để kiểm tra sản phẩm và lấy giá hiện tại. */
    private final ProductRepository productRepository;
    /** Repository dùng để xác định tài khoản từ tên đăng nhập trong phiên. */
    private final AccountRepository accountRepository;

    /** Khởi tạo service với các repository cần cho nghiệp vụ giỏ hàng. */
    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductRepository productRepository,
                           AccountRepository accountRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
    }

    /** Đọc giỏ hàng hiện tại và tính lại thành tiền theo giá sản phẩm hiện tại. */
    @Override
    @Transactional(readOnly = true)
    public CartResponse getCurrentCart() {
        Carts cart = getCurrentCartEntity();
        return toResponse(cart == null ? new ArrayList<CartItems>() : cartItemRepository.findByCart(cart));
    }

    /** Thêm một sản phẩm; sản phẩm trùng sẽ được cộng dồn số lượng. */
    @Override
    @Transactional
    public CartResponse addItem(CartItemRequest request) {
        Carts cart = getOrCreateCurrentCart();
        Products product = getAvailableProduct(request.getProductId());
        CartItems item = cartItemRepository.findByCartAndProduct(cart, product);

        if (item == null) {
            item = new CartItems();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            item.setCreatedAt(LocalDateTime.now());
            item.setIsDelete(false);
        } else {
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.setUpdatedAt(LocalDateTime.now());
        }
        cartItemRepository.save(item);
        return toResponse(cartItemRepository.findByCart(cart));
    }

    /** Cập nhật số lượng tuyệt đối của một sản phẩm đã có trong giỏ. */
    @Override
    @Transactional
    public CartResponse updateItem(Integer productId, CartItemRequest request) {
        if (!productId.equals(request.getProductId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "productId trên URL và body phải giống nhau");
        }
        Carts cart = getCurrentCartEntity();
        if (cart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Giỏ hàng trống");
        }
        Products product = getAvailableProduct(productId);
        CartItems item = cartItemRepository.findByCartAndProduct(cart, product);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sản phẩm không có trong giỏ hàng");
        }
        item.setQuantity(request.getQuantity());
        item.setUpdatedAt(LocalDateTime.now());
        cartItemRepository.save(item);
        return toResponse(cartItemRepository.findByCart(cart));
    }

    /** Xóa một dòng sản phẩm khỏi giỏ; an toàn nếu giỏ hoặc sản phẩm không còn tồn tại. */
    @Override
    @Transactional
    public void removeItem(Integer productId) {
        Carts cart = getCurrentCartEntity();
        if (cart == null) {
            return;
        }
        Products product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return;
        }
        CartItems item = cartItemRepository.findByCartAndProduct(cart, product);
        if (item != null) {
            cartItemRepository.delete(item);
        }
    }

    /** Lấy entity giỏ hàng theo tài khoản của phiên đăng nhập hiện tại. */
    @Override
    public Carts getCurrentCartEntity() {
        return cartRepository.findByAccount(getCurrentAccount());
    }

    /** Tạo giỏ mới cho tài khoản nếu tài khoản chưa có giỏ hoạt động. */
    private Carts getOrCreateCurrentCart() {
        Carts cart = getCurrentCartEntity();
        if (cart == null) {
            cart = new Carts();
            cart.setAccount(getCurrentAccount());
            cart.setCreatedAt(LocalDateTime.now());
            cart.setIsDelete(false);
            cart = cartRepository.save(cart);
        }
        return cart;
    }

    /** Đọc authentication hiện tại và truy vấn account tương ứng trong database. */
    private Accounts getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa đăng nhập");
        }
        Accounts account = accountRepository.findByUserName(authentication.getName());
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản không tồn tại");
        }
        return account;
    }

    /** Kiểm tra sản phẩm tồn tại và không bị xóa mềm trước khi cho vào giỏ. */
    private Products getAvailableProduct(Integer productId) {
        Products product = productRepository.findById(productId).orElse(null);
        if (product == null || Boolean.TRUE.equals(product.getIsDelete())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm có id = " + productId);
        }
        return product;
    }

    /** Chuyển entity giỏ hàng sang DTO, đồng thời tính subtotal và tổng tiền cho frontend. */
    private CartResponse toResponse(List<CartItems> cartItems) {
        List<CartItemResponse> items = new ArrayList<>();
        double totalAmount = 0;
        for (CartItems cartItem : cartItems) {
            Products product = cartItem.getProduct();
            double subtotal = product.getPrice() * cartItem.getQuantity();
            CartItemResponse item = new CartItemResponse();
            item.setProductId(product.getId());
            item.setCode(product.getCode());
            item.setName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(cartItem.getQuantity());
            item.setSubtotal(subtotal);
            items.add(item);
            totalAmount += subtotal;
        }
        CartResponse response = new CartResponse();
        response.setItems(items);
        response.setTotalAmount(totalAmount);
        return response;
    }
}

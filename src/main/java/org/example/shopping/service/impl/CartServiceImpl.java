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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.math.BigDecimal;
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
    private final HttpSession httpSession;

    /** Khởi tạo service với các repository cần cho nghiệp vụ giỏ hàng. */
    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductRepository productRepository,
                           AccountRepository accountRepository,
                           HttpSession httpSession) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
        this.httpSession = httpSession;
    }

    @Override
    /**
     * Gộp giỏ hàng tạm thời của session anonymous vào giỏ hàng của tài khoản đã đăng nhập.
     *
     * @param sessionId mã session trước khi đăng nhập
     * @param accountAccount tài khoản nhận giỏ hàng sau khi đăng nhập
     */
    public void mergeSessionCartIntoAccount(String sessionId, org.example.shopping.entity.Accounts accountAccount) {
        if (sessionId == null || accountAccount == null) return;
        Carts sessionCart = cartRepository.findBySessionId(sessionId);
        if (sessionCart == null) return;

        Carts accountCart = cartRepository.findByAccount(accountAccount);
        if (accountCart == null) {
            // assign session cart to account
            sessionCart.setAccount(accountAccount);
            sessionCart.setSessionId(null);
            cartRepository.save(sessionCart);
            httpSession.removeAttribute("CART_ID");
            return;
        }

        // Merge items: add quantities to accountCart, or move items
        java.util.List<CartItems> sessionItems = cartItemRepository.findByCart(sessionCart);
        for (CartItems si : sessionItems) {
            Products p = si.getProduct();
            CartItems existing = cartItemRepository.findByCartAndProduct(accountCart, p);
            if (existing == null) {
                si.setCart(accountCart);
                cartItemRepository.save(si);
            } else {
                existing.setQuantity(existing.getQuantity() + si.getQuantity());
                existing.setUpdatedAt(LocalDateTime.now());
                cartItemRepository.save(existing);
                cartItemRepository.delete(si);
            }
        }
        // delete session cart record
        cartRepository.delete(sessionCart);
        httpSession.removeAttribute("CART_ID");
    }

    /**
     * Đọc giỏ hàng hiện tại và tính lại thành tiền theo giá sản phẩm hiện tại.
     *
     * @return DTO chứa danh sách sản phẩm trong giỏ và tổng tiền
     */
    @Override
    @Transactional(readOnly = true)
    public CartResponse getCurrentCart() {
        Carts cart = getCurrentCartEntity();
        return toResponse(cart == null ? new ArrayList<CartItems>() : cartItemRepository.findByCart(cart));
    }

    /**
     * Thêm một sản phẩm vào giỏ hàng; nếu sản phẩm đã tồn tại thì tăng số lượng thay vì tạo bản ghi mới.
     *
     * @param request thông tin sản phẩm và số lượng cần thêm
     * @return DTO giỏ hàng mới sau khi cập nhật
     */
    @Override
    @Transactional
    public CartResponse addItem(CartItemRequest request) {
        validateQuantity(request.getQuantity());

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

        try {
            cartItemRepository.save(item);
        } catch (DataIntegrityViolationException ex) {
            // Xử lý trường hợp cạnh tranh insert cùng product và cart
            CartItems existing = cartItemRepository.findByCartAndProduct(cart, product);
            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + request.getQuantity());
                existing.setUpdatedAt(LocalDateTime.now());
                cartItemRepository.save(existing);
            } else {
                throw ex;
            }
        }

        return toResponse(cartItemRepository.findByCart(cart));
    }

    /**
     * Cập nhật số lượng tuyệt đối của một sản phẩm đã có trong giỏ hàng.
     *
     * @param productId id sản phẩm trên URL
     * @param request thông tin số lượng mới
     * @return DTO giỏ hàng sau khi cập nhật
     */
    @Override
    @Transactional
    public CartResponse updateItem(Integer productId, CartItemRequest request) {
        validateQuantity(request.getQuantity());

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

    /**
     * Xóa một dòng sản phẩm khỏi giỏ hàng; phương thức này an toàn nếu giỏ hoặc sản phẩm không còn tồn tại.
     *
     * @param productId id sản phẩm cần xóa khỏi giỏ
     */
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
        Accounts account = getCurrentAccountOrNull();
        if (account != null) {
            return cartRepository.findByAccount(account);
        }
        // anonymous: try session cart
        String sessionCartId = (String) httpSession.getAttribute("CART_ID");
        if (sessionCartId != null) {
            Carts c = cartRepository.findBySessionId(sessionCartId);
            if (c != null) return c;
        }
        return null;
    }

    /** Tạo giỏ mới cho tài khoản nếu tài khoản chưa có giỏ hoạt động. */
    private Carts getOrCreateCurrentCart() {
        Carts cart = getCurrentCartEntity();
        Accounts account = getCurrentAccountOrNull();
        if (cart == null) {
            cart = new Carts();
            cart.setAccount(account);
            cart.setCreatedAt(LocalDateTime.now());
            cart.setIsDelete(false);
            if (account == null) {
                // create session id and attach
                String sid = java.util.UUID.randomUUID().toString();
                cart.setSessionId(sid);
            }
            cart = cartRepository.save(cart);
            if (account == null) {
                httpSession.setAttribute("CART_ID", cart.getSessionId());
            }
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
        Accounts account = accountRepository.findByUserNameAndIsDeleteFalse(authentication.getName());
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản không tồn tại");
        }
        return account;
    }

    /** Trả về Account nếu authenticated, hoặc null nếu anonymous. */
    private Accounts getCurrentAccountOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        return accountRepository.findByUserNameAndIsDeleteFalse(authentication.getName());
    }

    /** Kiểm tra sản phẩm tồn tại và không bị xóa mềm trước khi cho vào giỏ. */
    private Products getAvailableProduct(Integer productId) {
        Products product = productRepository.findById(productId).orElse(null);
        if (product == null || Boolean.TRUE.equals(product.getIsDelete())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm có id = " + productId);
        }
        return product;
    }

    /** Chấp nhận quantity = 0 nhưng từ chối số âm và giá trị không hợp lệ. */
    private void validateQuantity(Integer quantity) {
        if (quantity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số lượng không được để trống");
        }
        if (quantity < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số lượng phải lớn hơn hoặc bằng 0");
        }
    }

    /** Chuyển entity giỏ hàng sang DTO, đồng thời tính subtotal và tổng tiền cho frontend. */
    private CartResponse toResponse(List<CartItems> cartItems) {
        List<CartItemResponse> items = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItems cartItem : cartItems) {
            Products product = cartItem.getProduct();
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            CartItemResponse item = new CartItemResponse();
            item.setProductId(product.getId());
            item.setCode(product.getCode());
            item.setName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(cartItem.getQuantity());
            item.setSubtotal(subtotal);
            items.add(item);
            totalAmount = totalAmount.add(subtotal);
        }
        CartResponse response = new CartResponse();
        response.setItems(items);
        response.setTotalAmount(totalAmount);
        return response;
    }
}

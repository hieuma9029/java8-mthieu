package org.example.shopping.service.impl;

import org.example.shopping.entity.BaseEntity;
import org.example.shopping.repository.BaseRepository;
import org.example.shopping.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.Serializable;
import java.util.List;

/**
 * Cài đặt chung cho các service CRUD cơ bản.
 *
 * <p>Giảm thiểu code trùng lặp bằng cách sử dụng {@link BaseRepository}
 * cho các thao tác tìm, lưu, cập nhật và xóa.</p>
 */
public abstract class BaseServiceImpl<T extends BaseEntity, ID extends Serializable, R extends BaseRepository<T, ID>>
        implements BaseService<T, ID> {

    protected final R repository;

    protected BaseServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    /**
     * Trả về toàn bộ thực thể đang tồn tại trong repository.
     *
     * @return danh sách thực thể
     */
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    /**
     * Trả về một trang dữ liệu của thực thể theo phân trang.
     *
     * @param page số trang bắt đầu từ 0
     * @param size số phần tử mỗi trang
     * @return trang dữ liệu tương ứng
     */
    public Page<T> findAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Override
    /** Tìm thực thể theo id; trả về {@code null} nếu không tồn tại. */
    public T findById(ID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    /** Lưu thực thể mới vào repository. */
    public void save(T entity) {
        repository.save(entity);
    }

    @Override
    /** Cập nhật thực thể đã tồn tại, sao chép dữ liệu từ thực thể nguồn. */
    public void update(ID id, T entity) {
        T existing = repository.findById(id).orElse(null);
        if (existing != null) {
            copyForUpdate(existing, entity);
            repository.save(existing);
        }
    }

    @Override
    /** Xóa thực thể theo id. */
    public void delete(ID id) {
        repository.deleteById(id);
    }

    /**
     * Sao chép dữ liệu cần thiết từ thực thể nguồn vào thực thể tồn tại.
     *
     * @param existing thực thể đã tồn tại trong database
     * @param source   thực thể chứa dữ liệu mới
     */
    protected abstract void copyForUpdate(T existing, T source);
}

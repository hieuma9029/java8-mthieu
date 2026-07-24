package org.example.shopping.repository;

import org.example.shopping.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Repository cha cho tất cả repository.
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID extends Serializable>
        extends JpaRepository<T, ID> {
}
package com.kiran.dao;

import com.kiran.model.entity.RetroEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * @author Kiran
 * @since 11/8/17
 */

@Component
public interface RetroDao extends CrudRepository<RetroEntity, Long> {

    Iterable<RetroEntity> findByIsActive(boolean isActive);

    Iterable<RetroEntity> findByIsActiveAndUserName(boolean isActive, String userName);

    Iterable<RetroEntity> findByUserName(String userName);

}

package com.kiran.dao;

import com.kiran.model.entity.DuckEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * @author Kiran
 * @since 12/18/17
 */
@Component
public interface DuckDao extends CrudRepository<DuckEntity, Long> {

    DuckEntity findByUserName(String userName);

}

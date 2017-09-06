package com.kiran.dao;

import com.kiran.model.entity.UserLogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * @author Kiran
 * @since 9/5/17
 */

@Component
public interface UserLogDao  extends CrudRepository<UserLogEntity, Long> {

    Iterable<UserLogEntity> findByUserName(String userName);

}

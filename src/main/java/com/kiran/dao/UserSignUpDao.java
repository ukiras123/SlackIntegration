package com.kiran.dao;

import com.kiran.model.entity.UserSignUpEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * @author Kiran
 * @since 9/29/17
 */

@Component
public interface UserSignUpDao extends CrudRepository<UserSignUpEntity, Long>  {

    UserSignUpEntity findByUserNameAndPassword(String userName, String password);

    UserSignUpEntity findByUserName(String userName);

}

package com.kiran.service;

import com.kiran.controller.dto.UserLoginDTO.UserSignUpDTO;
import com.kiran.dao.UserSignUpDao;
import com.kiran.model.entity.UserSignUpEntity;
import com.kiran.translator.UserSignUpTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kiran
 * @since 9/29/17
 */

@Service
public class UserSignUpService {


    @Autowired
    private UserSignUpDao userSignUpDao;

    @Autowired
    private UserSignUpTranslator userSignUpTranslator;

    public UserSignUpEntity signUpUser(UserSignUpDTO userSignUpDTO) {
        UserSignUpEntity newUser = userSignUpTranslator.dtoToEntity(userSignUpDTO);
        return userSignUpDao.save(newUser);
    }

    public UserSignUpEntity readUserByUserAndPass(String userName, String password) {
        return userSignUpDao.findByUserNameAndPassword(userName, password);
    }

    public UserSignUpEntity readUserByUser(String userName) {
        return userSignUpDao.findByUserName(userName);
    }
}

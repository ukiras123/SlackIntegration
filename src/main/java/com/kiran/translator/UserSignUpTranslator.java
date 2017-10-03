package com.kiran.translator;

import com.kiran.controller.dto.UserLoginDTO.UserSignUpDTO;
import com.kiran.model.entity.UserSignUpEntity;
import org.springframework.stereotype.Component;

/**
 * @author Kiran
 * @since 9/29/17
 */

@Component
public class UserSignUpTranslator {

    public UserSignUpEntity dtoToEntity (final UserSignUpDTO userSignUpDTO)
    {
        UserSignUpEntity userSignUpEntity = new UserSignUpEntity();
        userSignUpEntity.setUserName(userSignUpDTO.getUserName());
        userSignUpEntity.setFirstName(userSignUpDTO.getFirstName());
        userSignUpEntity.setLastName(userSignUpDTO.getLastName());
        userSignUpEntity.setEmail(userSignUpDTO.getEmail());
        userSignUpEntity.setPassword(userSignUpDTO.getPassword());
        userSignUpEntity.setJiraUser(userSignUpDTO.getJiraUser());
        userSignUpEntity.setJiraPassword(userSignUpDTO.getJiraPassword());
        return userSignUpEntity;
    }

    public UserSignUpDTO entityToDTO(UserSignUpEntity userSignUpEntity) {
        UserSignUpDTO userSignUpDTO = new UserSignUpDTO();
        userSignUpDTO.setUserName(userSignUpEntity.getUserName());
        userSignUpDTO.setFirstName(userSignUpEntity.getFirstName());
        userSignUpDTO.setLastName(userSignUpEntity.getLastName());
        userSignUpDTO.setEmail(userSignUpEntity.getEmail());
        userSignUpDTO.setPassword(userSignUpEntity.getPassword());
        userSignUpDTO.setJiraUser(userSignUpEntity.getJiraUser());
        userSignUpDTO.setJiraPassword(userSignUpEntity.getJiraPassword());
        return userSignUpDTO;
    }

}

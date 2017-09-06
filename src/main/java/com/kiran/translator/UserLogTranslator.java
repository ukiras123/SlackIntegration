package com.kiran.translator;

import com.kiran.controller.dto.UserLogDTO.UserLogDTO;
import com.kiran.model.entity.UserLogEntity;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Kiran
 * @since 9/5/17
 */

@Component
public class UserLogTranslator {

    public UserLogEntity dtoToEntity (final UserLogDTO userInfoDTO)
    {
        UserLogEntity UserInfoEntity = new UserLogEntity();
        UserInfoEntity.setUserName(userInfoDTO.getUserName());
        UserInfoEntity.setTimeStamp(userInfoDTO.getTimeStamp());
        UserInfoEntity.setInfo(userInfoDTO.getInfo());
        return UserInfoEntity;
    }

    public UserLogDTO entityToDTO(UserLogEntity userLogEntity) {
        UserLogDTO userInfoDTO = new UserLogDTO();
        userInfoDTO.setUserName(userLogEntity.getUserName());
        userInfoDTO.setTimeStamp(userLogEntity.getTimeStamp());
        userInfoDTO.setInfo(userLogEntity.getInfo());
        return userInfoDTO;
    }

    public List<UserLogDTO> entityListToDTOList(Iterable<UserLogEntity> iterable) {
        LinkedList list = new LinkedList();
        Iterator iter = iterable.iterator();
        while(iter.hasNext()) {
            list.add(this.entityToDTO((UserLogEntity) iter.next()));
        }
        return list;
    }
}

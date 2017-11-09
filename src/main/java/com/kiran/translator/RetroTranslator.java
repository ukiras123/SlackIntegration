package com.kiran.translator;

import com.kiran.controller.dto.RetroDTO.RetroDTO;
import com.kiran.model.entity.RetroEntity;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Kiran
 * @since 11/8/17
 */

@Component
public class RetroTranslator {

    public RetroEntity dtoToEntity (final RetroDTO retroDTO)
    {
        RetroEntity retroEntity = new RetroEntity();
        retroEntity.setUserName(retroDTO.getUserName());
        retroEntity.setRetroMessage(retroDTO.getRetroMessage());
        retroEntity.setActive(retroDTO.isActive());
        retroEntity.setTimeStamp(retroDTO.getTimeStamp());
        return retroEntity;
    }

    public RetroDTO entityToDTO(RetroEntity retroEntity) {
        RetroDTO retroDTO = new RetroDTO();
        retroDTO.setUserName(retroEntity.getUserName());
        retroDTO.setRetroMessage(retroEntity.getRetroMessage());
        retroDTO.setActive(retroEntity.isActive());
        retroDTO.setTimeStamp(retroEntity.getTimeStamp());
        return retroDTO;
    }

    public List<RetroDTO> entityListToDTOList(Iterable<RetroEntity> iterable) {
        LinkedList list = new LinkedList();
        Iterator iter = iterable.iterator();
        while(iter.hasNext()) {
            list.add(this.entityToDTO((RetroEntity) iter.next()));
        }
        return list;
    }

}

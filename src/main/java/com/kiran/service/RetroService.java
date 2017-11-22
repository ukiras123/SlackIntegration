package com.kiran.service;

import com.kiran.controller.dto.RetroDTO.RetroDTO;
import com.kiran.dao.RetroDao;
import com.kiran.model.entity.RetroEntity;
import com.kiran.translator.RetroTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kiran
 * @since 11/8/17
 */

@Component
public class RetroService {

    @Autowired
    private RetroDao retroDao;

    @Autowired
    private RetroTranslator retroTranslator;

    public RetroEntity createRetro(RetroDTO retroDTO) {
        RetroEntity newRetro = retroTranslator.dtoToEntity(retroDTO);
        return retroDao.save(newRetro);
    }

    public RetroEntity createRetro(RetroEntity retroEntity) {
        return retroDao.save(retroEntity);
    }

    public List<RetroEntity> readAllActiveRetro() {
        List<RetroEntity> list = new ArrayList<RetroEntity>();
        for (RetroEntity retro :  retroDao.findByIsActive(true)) {
            list.add(retro);
        }
        return list;
    }

    public List<RetroEntity> readAllActiveRetro(String userName) {
        List<RetroEntity> list = new ArrayList<RetroEntity>();
        for (RetroEntity retro :  retroDao.findByIsActiveAndUserName(true, userName)) {
            list.add(retro);
        }
        return list;
    }

    public Iterable<RetroEntity> readUserName(final String userName) {
        return retroDao.findByUserName(userName);
    }


}

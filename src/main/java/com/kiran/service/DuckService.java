package com.kiran.service;

import com.kiran.controller.dto.DuckDTO.DuckDTO;
import com.kiran.dao.DuckDao;
import com.kiran.model.entity.DuckEntity;
import com.kiran.service.utilities.Constant;
import com.kiran.translator.DuckTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kiran
 * @since 12/18/17
 */

@Component
public class DuckService {


    @Autowired
    private DuckDao duckDao;

    @Autowired
    private DuckTranslator duckTranslator;


    public List<DuckEntity> readAllDuck() {
        List<DuckEntity> list = new ArrayList<DuckEntity>();
        for (DuckEntity duck :  duckDao.findAll()) {
            list.add(duck);
        }
        return list;
    }

    public DuckEntity readByUserName(String userName) {
        DuckEntity newDuck = duckDao.findByUserName(userName);
        if (newDuck == null) {
            newDuck = new DuckEntity(userName, 10);
        }
        return newDuck;
    }

    public DuckEntity addUpdateDuck(DuckDTO duckDTO) {
        DuckEntity newDuck = duckDao.findByUserName(duckDTO.getUserName());
        if (newDuck == null) {
            newDuck = duckTranslator.dtoToEntity(duckDTO);
            newDuck.setUserName(duckDTO.getUserName());
        }
        newDuck.setTotalDuck(duckDTO.getTotalDuck());
        return duckDao.save(newDuck);
    }


    public String giveDuckCalculation(String giverUserName, String receiverUserName) {
        DuckEntity giver = readByUserName(giverUserName);
        DuckEntity receiver = readByUserName(receiverUserName);
        if (giverUserName.equalsIgnoreCase(receiverUserName)) {
            return "Come on now, you can not give :duck: to yourself. Don't try to cheat here. :angry:";
        } else {
            if (giver.getTotalDuck() < 1) {
                return "You do not have enough :duck: to give away. Work hard to earn. :grimacing:";
            } else {
                DuckDTO giverDTO = duckTranslator.entityToDTO(giver);
                DuckDTO receiverDTO = duckTranslator.entityToDTO(receiver);
                giverDTO.removeDuck();
                receiverDTO.addDuck();
                addUpdateDuck(giverDTO);
                addUpdateDuck(receiverDTO);
                return ">Congratulations <@" + receiverUserName + ">, you just got a new duck from <@" + giverUserName + ">.\n" +
                        ">You have now `" + receiverDTO.getTotalDuck() + "` :duck:. Good job.\n"+
                        "*" + Constant.getACompliment() +"*" ;
            }
        }
    }

    public boolean hasEnoughDuck(String userName) {
        DuckEntity user = readByUserName(userName);
        return user.getTotalDuck() >= 1;
    }

    public int getDuckCount(String userName) {
        DuckEntity user = readByUserName(userName);
        return user.getTotalDuck();
    }

    public void giveTakeDuck(String giverUserName, String receiverUserName)
    {
        DuckEntity giver = readByUserName(giverUserName);
        DuckEntity receiver = readByUserName(receiverUserName);
        DuckDTO giverDTO = duckTranslator.entityToDTO(giver);
        DuckDTO receiverDTO = duckTranslator.entityToDTO(receiver);
        giverDTO.removeDuck();
        receiverDTO.addDuck();
        addUpdateDuck(giverDTO);
        addUpdateDuck(receiverDTO);
    }

    public void giveDuck(String user)
    {
        DuckEntity duckUser = readByUserName(user);
        DuckDTO duckUserDTO = duckTranslator.entityToDTO(duckUser);
        duckUserDTO.addDuck();
        addUpdateDuck(duckUserDTO);
    }

    public void takeDuck(String user)
    {
        DuckEntity duckUser = readByUserName(user);
        DuckDTO duckUserDTO = duckTranslator.entityToDTO(duckUser);
        duckUserDTO.removeDuck();
        addUpdateDuck(duckUserDTO);
    }

    public String geDuckWinner() {
        List<DuckEntity> entities = duckDao.findByTotalDuck();
        if (entities == null) {
            return "There is not any winner";
        } else {
            String user = "";
            for (DuckEntity e : entities) {
                user += "><@"+e.getUserName()+"> : "+e.getTotalDuck()+" :duck: \n";
            }
            return "*Top 3 Duck Holders:*\n" + user;
        }
    }
    public String getMyDuckDetail(String userName) {
        DuckEntity user = readByUserName(userName);
        if (user.getTotalDuck() >= 4) {
            return "You have `"+user.getTotalDuck()+"` :duck:. Be generous to share.";
        }
        return "You have `"+user.getTotalDuck()+"` :duck:. Work hard to earn more.";
    }
}

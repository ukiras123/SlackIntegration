package com.kiran.translator;

import com.kiran.controller.dto.DuckDTO.DuckDTO;
import com.kiran.model.entity.DuckEntity;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Kiran
 * @since 12/18/17
 */

@Component
public class DuckTranslator {

    public DuckEntity dtoToEntity (final DuckDTO duckDTO)
    {
        DuckEntity duckEntity = new DuckEntity();
        duckEntity.setUserName(duckDTO.getUserName());
        duckEntity.setTotalDuck(duckDTO.getTotalDuck());
        return duckEntity;
    }

    public DuckDTO entityToDTO(DuckEntity duckEntity) {
        DuckDTO duckDTO = new DuckDTO();
        duckDTO.setUserName(duckEntity.getUserName());
        duckDTO.setTotalDuck(duckEntity.getTotalDuck());
        return duckDTO;
    }

    public List<DuckDTO> entityListToDTOList(Iterable<DuckEntity> iterable) {
        LinkedList list = new LinkedList();
        Iterator iter = iterable.iterator();
        while(iter.hasNext()) {
            list.add(this.entityToDTO((DuckEntity) iter.next()));
        }
        return list;
    }
}

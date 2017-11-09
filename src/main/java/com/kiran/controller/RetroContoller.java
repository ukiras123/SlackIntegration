package com.kiran.controller;

import com.kiran.controller.dto.RetroDTO.RetroDTO;
import com.kiran.model.entity.RetroEntity;
import com.kiran.model.response.ReadAllRetroResponse;
import com.kiran.model.response.RetroResponse;
import com.kiran.service.RetroService;
import com.kiran.translator.RetroTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Kiran
 * @since 11/8/17
 */

@RestController
@RequestMapping("/retro")
public class RetroContoller {

    @Autowired
    private RetroService retroService;

    @Autowired
    private RetroTranslator retroTranslator;

    //Read all message
    @RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> readRetros() {
        Iterable<RetroEntity> retroEntityIterable = retroService.readAllActiveRetro();
        List<RetroDTO> retroEntityList =
                retroTranslator.entityListToDTOList(retroEntityIterable);
        ReadAllRetroResponse response = new ReadAllRetroResponse(retroEntityList);
        return new ResponseEntity<>(response, null, HttpStatus.OK);
    }

    //CreateRetro
    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createRetro(@RequestBody RetroDTO retroDTO) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String timeStamp = dateFormat.format(date);
        retroDTO.setTimeStamp(timeStamp);
        retroDTO.setActive(true);
        retroService.createRetro(retroDTO);
        RetroResponse response = new RetroResponse(retroDTO.getUserName(), retroDTO.getRetroMessage());
        return new ResponseEntity<RetroResponse>(response,null, HttpStatus.CREATED);
    }

    //Clear all message
    @RequestMapping(value = "/clear",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> clearRetros() {
        Iterable<RetroEntity> retroEntityIterable = retroService.readAllActiveRetro();
        for (RetroEntity e : retroEntityIterable) {
            e.setActive(false);
            retroService.createRetro(e);
        }
        return new ResponseEntity<>(null, null, HttpStatus.OK);
    }

}

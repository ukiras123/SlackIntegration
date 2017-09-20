package com.kiran.controller;

import com.kiran.controller.dto.UserLogDTO.UserLogDTO;
import com.kiran.model.entity.UserLogEntity;
import com.kiran.model.response.ReadAllLogResponse;
import com.kiran.service.UserLogService;
import com.kiran.translator.UserLogTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Kiran
 * @since 9/5/17
 */

@RestController
@RequestMapping("/users")
public class UserLogController {

    @Autowired
    private UserLogTranslator userLogTranslator;

    //Read user by userName
    @RequestMapping(value = "/{userName}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> readAllUsersByUserName(@PathVariable("userName") String userName) {
        Iterable<UserLogEntity> userLogEntityIterable = userLogService.readUserByFirstName(userName);
        List<UserLogDTO> userILogEntityList =
                userLogTranslator.entityListToDTOList(userLogEntityIterable);
        ReadAllLogResponse response = new ReadAllLogResponse(userILogEntityList.size(), userILogEntityList);
        return new ResponseEntity<>(response, null, HttpStatus.OK);
    }

    //Read all users
    @RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> readMarketingProgram() {
        Iterable<UserLogEntity> userLogEntityIterable = userLogService.readAllUsers();
        List<UserLogDTO> userILogEntityList =
                userLogTranslator.entityListToDTOList(userLogEntityIterable);
        ReadAllLogResponse response = new ReadAllLogResponse(userILogEntityList.size(), userILogEntityList);
        return new ResponseEntity<>(response, null, HttpStatus.OK);
    }


}

package com.kiran.controller;

import com.kiran.controller.dto.UserLoginDTO.UserLoginDTO;
import com.kiran.controller.dto.UserLoginDTO.UserSignUpDTO;
import com.kiran.model.entity.UserSignUpEntity;
import com.kiran.model.response.GenericErrorResponse;
import com.kiran.model.response.UserSignUpResponse;
import com.kiran.service.UserSignUpService;
import com.kiran.translator.UserSignUpTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kiran
 * @since 9/29/17
 */


@RestController
@RequestMapping("/user")
public class UserLoginController {


    @Autowired
    private UserSignUpService userSignUpService;

    @Autowired
    private UserSignUpTranslator userSignUpTranslator;

    //Sign-Up
    @RequestMapping(value = "/signup",method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> signUp(@RequestBody UserSignUpDTO userSignUpDTO) {
        UserSignUpEntity signUpEntity = userSignUpService.readUserByUser(userSignUpDTO.getUserName());
        if (signUpEntity == null) {
            signUpEntity = userSignUpService.signUpUser(userSignUpDTO);
            UserSignUpResponse response = new UserSignUpResponse(signUpEntity.getUserName(), signUpEntity.getFirstName(), signUpEntity.getLastName());
            return new ResponseEntity<UserSignUpResponse>(response,null, HttpStatus.CREATED);
        } else {
            GenericErrorResponse response = new GenericErrorResponse("User name already exist");
            return new ResponseEntity<GenericErrorResponse>(response,null, HttpStatus.BAD_REQUEST);
        }
    }

    //Login
    @RequestMapping(value = "/login", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {
        UserSignUpEntity signUpEntity = userSignUpService.readUserByUserAndPass(userLoginDTO.getUserName(), userLoginDTO.getPassword());
        if (signUpEntity == null) {
            GenericErrorResponse response = new GenericErrorResponse("User/Pass not correct");
            return new ResponseEntity<GenericErrorResponse>(response,null, HttpStatus.BAD_REQUEST);
        } else {
            UserSignUpResponse response = new UserSignUpResponse(signUpEntity.getUserName(), signUpEntity.getFirstName(), signUpEntity.getLastName());
            return new ResponseEntity<UserSignUpResponse>(response,null, HttpStatus.OK);
        }
    }

}

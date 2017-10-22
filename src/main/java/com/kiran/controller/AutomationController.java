package com.kiran.controller;

import com.kiran.controller.dto.SlateUIDTO.SlateUIDTO;
import com.kiran.model.response.GenericErrorResponse;
import com.kiran.model.response.SlackResponse;
import com.kiran.service.exception.InvalidMove;
import com.kiran.service.regressionTest.RegressionTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kiran
 * @since 10/21/17
 */

@RestController
@RequestMapping("/automation")
public class AutomationController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RegressionTest regressionTest;


    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<?> runAutomation(@RequestBody SlateUIDTO slateUIDTO) throws InterruptedException {
        logger.info("Inside /automation controller------------------------------------");
        try {
            logger.info(slateUIDTO.toString());
            String report = regressionTest.doRegression(slateUIDTO.getApiName(), slateUIDTO.getBranch(), slateUIDTO.getEmail(), slateUIDTO.getAutomationLevel());
            SlackResponse responseOk = new SlackResponse(report);
            return new ResponseEntity<>(responseOk, null, HttpStatus.OK);
        } catch (InvalidMove e) {
            GenericErrorResponse errorResponse = new GenericErrorResponse(e.getError_message());
            logger.error(e.getMessage());
            return new ResponseEntity<>(errorResponse, null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            GenericErrorResponse errorResponse = new GenericErrorResponse("Something went wrong. Please contact your administrator.");
            logger.error(e.getMessage());
            return new ResponseEntity<>(errorResponse, null, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Exiting /automation controller------------------------------------");
        }
    }

}

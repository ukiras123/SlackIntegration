package com.kiran.controller.intercept;

import com.kiran.model.response.SlackResponse;
import com.kiran.service.exception.InvalidMove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Kiran
 * @since 9/12/17
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({InvalidMove.class})
    public ResponseEntity<?> handleInvalidMoveException(InvalidMove move) {
        SlackResponse response = new SlackResponse(move.getError_message());
        return new ResponseEntity<>(response, null, HttpStatus.OK);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        this.logger.error("Internal slate error. ExceptionMessage=\'{}\'. StackTrace=\'{}\'", ex.getMessage(), ex.getStackTrace());
        SlackResponse response = new SlackResponse("Something went wrong, please contact your administrator");
        return new ResponseEntity<>(response, null, HttpStatus.OK);
    }


}

package com.kiran.service.integration;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kiran
 * @since 1/8/18
 */

@Component
public class TriviaAPI extends  BaseApiCall{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${trivia.url}")
    private String triviaUrl;
    @Value("${trivia.token}")
    private String triviaToken;
    @Value("${trivia.reset_url}")
    private String triviaResetUrl;


    public JSONObject getTrivia(int categoryId) throws InterruptedException {
        logger.info("Inside getTrivia------------------------------------");
        Map<String, String> header = new HashMap<>();
        String amountFilter = "amount=1&";
        String categoryFilter = "category="+categoryId+"&" ;
        String finalUrl = triviaUrl + amountFilter + categoryFilter;
        JSONObject jBody = apiGetCall(finalUrl, header);
        int responseCount = jBody.getJSONArray("results").length();
        while (responseCount == 0) {
            jBody = apiGetCall(finalUrl, header);
            responseCount = jBody.getJSONArray("results").length();
            logger.info("-------------  Response was empty so reset token  --------------------");

        }
        return jBody;
    }

    private void resetToken() throws InterruptedException {
        logger.info("Inside resetToken------------------------------------");
        Map<String, String> header = new HashMap<>();
        String tokenFilter = "&token="+triviaToken;
        String finalUrl = triviaUrl + tokenFilter;
        apiGetCall(finalUrl, header);
    }


}

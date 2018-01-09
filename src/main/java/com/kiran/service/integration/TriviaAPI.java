package com.kiran.service.integration;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kiran
 * @since 1/8/18
 */

@Component
public class TriviaAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${trivia.url}")
    private String triviaUrl;
    @Value("${trivia.token}")
    private String triviaToken;
    @Value("${trivia.reset_url}")
    private String triviaResetUrl;

    private JSONObject apiGetCall(String url, Map<String, String> header) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            headers.add(entry.getKey(), entry.getValue());
        }
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        JSONObject jsonObject = new JSONObject(response);
        String strBody = jsonObject.getString("body");
        JSONObject jBody = new JSONObject(strBody);
        return jBody;
    }

    public JSONObject getTrivia(int categoryId) throws InterruptedException {
        logger.info("Inside getTrivia------------------------------------");
        Map<String, String> header = new HashMap<>();
        String amountFilter = "amount=1&";
        String categoryFilter = "category="+categoryId+"&" ;
        String tokenFilter = "token="+triviaToken+"&" ;
        String finalUrl = triviaUrl + amountFilter + categoryFilter + tokenFilter;
        JSONObject jBody = apiGetCall(finalUrl, header);
        int responseCount = jBody.getJSONArray("results").length();
        while (responseCount == 0) {
            resetToken();
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

package com.kiran.service.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Kiran
 * @since 10/5/17
 */

@Component
public class SlackAPI {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

     public void sendMessage(String message, String user) {
         String URL = "https://hooks.slack.com/services/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";
         String kiran_url = "https://hooks.slack.com/services/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";
         String imageAdd = "https://cdn.pixabay.com/photo/2016/08/25/07/30/red-1618916_960_720.png";
        try {
            String payload = "{\"text\":\"" + message + "\",\"username\":\"" + user + "\",\"icon_url\":\"" + imageAdd + "\"}";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<String>(payload,headers);
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, request, String.class);
        } catch (Exception ex) {
            this.logger.error("Exception during Jira API call. ExceptionMessage=\'{}\'. StackTrace=\'{}\'", ex.getMessage(), ex.getStackTrace());
        }
    }

}

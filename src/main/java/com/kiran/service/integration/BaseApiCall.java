package com.kiran.service.integration;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Kiran
 * @since 1/31/18
 */
public class BaseApiCall {

    public JSONObject apiGetCall(String url, Map<String, String> header) throws InterruptedException {
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
}

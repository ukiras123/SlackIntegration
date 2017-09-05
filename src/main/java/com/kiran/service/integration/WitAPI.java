package com.kiran.service.integration;

import com.kiran.service.exception.InvalidMove;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @author Kiran
 * @since 9/3/17
 */

@Component
public class WitAPI {

    @Value("${wit.url}")
    private String witUrl;
    @Value("${wit.auth}")
    private String witAuth;


    public HashMap<String, String> understandMe(String userInput) throws InterruptedException {
        String URL = witUrl + userInput;

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", witAuth);
            HttpEntity<String> request = new HttpEntity<String>(headers);
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, request, String.class);
            JSONObject jObject = new JSONObject(response.getBody());
            HashMap<String, String> entities = new HashMap<>();
            getEntities(jObject, entities);
            return entities;
        } catch (Exception ex) {
            throw new InvalidMove("I didn't understand you.");
        }
    }

    private void getEntities(JSONObject jsonObj, HashMap<String,String> entities) throws InterruptedException {
        try {
            JSONObject jsonEntities = null;
            if (jsonObj.getJSONObject("entities").length() != 0) {
                jsonEntities = jsonObj.getJSONObject("entities");
            } else {
                throw new InvalidMove("I need location to find the restaurants.");
            }
            for (int i = 0; i < jsonEntities.length();i++) {
                String entityname = jsonEntities.names().get(i).toString();
                JSONArray ja = jsonEntities.getJSONArray(entityname);
                entities.put(entityname, ja.getJSONObject(0).getString("value"));
            }
        } catch (Exception ex) {
            throw new InvalidMove("I didn't understand you.");
        }
    }


}

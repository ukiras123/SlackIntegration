package com.kiran.service.integration;

import org.json.JSONArray;
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

/**
 * @author Kiran
 * @since 11/14/17
 */

@Component
public class DictionaryAPI {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${dictionary.meaningUrl}")
    private String meaningUrl;
    @Value("${dictionary.sentenceUrl}")
    private String sentenceUrl;
    @Value("${dictionary.app_id}")
    private String dictionaryAppId;
    @Value("${dictionary.app_key}")
    private String dictionaryAppKey;


    public String getMeaning(String word) throws InterruptedException {
        String URL = String.format(meaningUrl, word);
        String meaning = "";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("app_id", dictionaryAppId);
        headers.add("app_key", dictionaryAppKey);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, request, String.class);
        JSONObject jObject = new JSONObject(response);
        String strBody = jObject.getString("body");
        JSONObject jBody = new JSONObject(strBody);
        JSONArray jArray = jBody.getJSONArray("results");
        meaning = jArray.getJSONObject(0).getJSONArray("lexicalEntries").getJSONObject(0).getJSONArray("entries").getJSONObject(0).getJSONArray("senses").getJSONObject(0).getJSONArray("definitions").get(0).toString();
        return meaning;
    }


    public String getSentence(String word) throws InterruptedException {
        String URL = String.format(sentenceUrl, word);
        String sentence = "";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("app_id", dictionaryAppId);
        headers.add("app_key", dictionaryAppKey);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, request, String.class);
        JSONObject jObject = new JSONObject(response);
        String strBody = jObject.getString("body");
        JSONObject jBody = new JSONObject(strBody);
        JSONArray jArray = jBody.getJSONArray("results");
        sentence = jArray.getJSONObject(0).getJSONArray("lexicalEntries").getJSONObject(0).getJSONArray("sentences").getJSONObject(0).getString("text");
        return sentence;
    }


}

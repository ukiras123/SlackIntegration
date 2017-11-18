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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Kiran
 * @since 11/17/17
 */

@Component
public class RandomAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${nyt.url}")
    private String ntyUrl;
    @Value("${nyt.api_key}")
    private String nytApiKey;
    @Value("${dadjoke.url}")
    private String dadjokeUrl;
    @Value("${chucknorris.url}")
    private String chuckUrl;
    @Value("${quote.url}")
    private String quoteUrl;


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

    public String getSurprise() throws InterruptedException {
        Random generator = new Random();
        int i = generator.nextInt(3) + 1;
        String surprise = "";
        if (i == 1) {
            surprise = getNews();
        } else if (i == 2) {
            surprise = getQuote();
        } else if (i == 3) {
            surprise = getDadJoke();
        } else {
            surprise = getChuckJoke();
        }
        return surprise;
    }


    public String getNews() throws InterruptedException {
        logger.info("Inside News Method------------------------------------");
        Map<String, String> header = new HashMap<>();
        header.put("api-key", nytApiKey);
        JSONObject jBody = apiGetCall(ntyUrl, header);
        JSONArray jsonArray = jBody.getJSONArray("results");
        Random random = new Random();
        int index = random.nextInt(jsonArray.length());
        String title = jsonArray.getJSONObject(index).getString("title");
        String detail = jsonArray.getJSONObject(index).getString("abstract");
        return "*-----Get Updated-----*\n" + "*" + title + "*\n" + detail;
    }


    public String getQuote() throws InterruptedException {
        logger.info("Inside Quote Method------------------------------------");
        Map<String, String> header = new HashMap<>();
        JSONObject jBody = apiGetCall(quoteUrl, header);
        String quote = jBody.getString("quote");
        return "*-----Be Happy-----*\n" + quote + "\n:wine_glass:";
    }

    public String getDadJoke() throws InterruptedException {
        logger.info("Inside Dad Joke Method------------------------------------");
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("User-Agent", "https://github.com/ukiras123/SlackIntegration");
        JSONObject jBody = apiGetCall(dadjokeUrl, header);
        String joke = jBody.getString("joke");
        return "*-----Smile-----*\n" + joke + "\n:wine_glass:";
    }

    public String getChuckJoke() throws InterruptedException {
        logger.info("Inside Chuck Joke Method------------------------------------");
        Map<String, String> header = new HashMap<>();
        JSONObject jBody = apiGetCall(chuckUrl, header);
        String joke = jBody.getString("value");
        return "*-----Smile-----*\n" + joke + "\n:wine_glass:";
    }


}

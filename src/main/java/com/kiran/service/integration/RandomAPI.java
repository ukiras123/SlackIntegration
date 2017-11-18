package com.kiran.service.integration;

import com.kiran.service.utilities.Name;
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
    @Value("${weather.url}")
    private String weatherUrl;
    @Value("${bitcoin.url}")
    private String bitcoinUrl;

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

    public String getSurprise() throws InterruptedException {
        Random generator = new Random();
        int i = generator.nextInt(6) + 1;
        String surprise = "";
        if (i == 1) {
            surprise = getNews();
        } else if (i == 2) {
            surprise = getQuote();
        } else if (i == 3) {
            surprise = getDadJoke();
        } else if (i == 4) {
            surprise = getChuckJoke(null);
        } else if (i == 5) {
            surprise = getBitCoin();
        } else {
            surprise = getWeather();
        }
        return surprise;
    }

    private String[] getRandomName() {
        Name[] names = Name.values();
        Random random = new Random();
        Name name = names[random.nextInt(names.length)];
        String[] returnString = {name.getFirstName(), name.getLastName()};
        return returnString;
    }

    private String getNews() throws InterruptedException {
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


    private String getQuote() throws InterruptedException {
        logger.info("Inside Quote Method------------------------------------");
        Map<String, String> header = new HashMap<>();
        JSONObject jBody = apiGetCall(quoteUrl, header);
        String quote = jBody.getString("quote");
        return "*-----Be Happy-----*\n" + quote;
    }

    private String getDadJoke() throws InterruptedException {
        logger.info("Inside Dad Joke Method------------------------------------");
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("User-Agent", "https://github.com/ukiras123/SlackIntegration");
        JSONObject jBody = apiGetCall(dadjokeUrl, header);
        String joke = jBody.getString("joke");
        return "*-----Smile-----*\n" + joke;
    }

    private String getWeather() throws InterruptedException {
        logger.info("Inside Weather Method------------------------------------");
        Map<String, String> header = new HashMap<>();
        JSONObject jBody = apiGetCall(weatherUrl, header);
        String currentSummary = jBody.getJSONObject("currently").getString("summary");
        String currentTemperature = jBody.getJSONObject("currently").getInt("temperature") + "°F";
        String dailySummary = jBody.getJSONObject("daily").getString("summary");
        return "*-----Weather-----*\n" + dailySummary + "\nCurrent: " + currentSummary + ", " + currentTemperature;
    }

    private String getBitCoin() throws InterruptedException {
        logger.info("Inside BitCoin Method------------------------------------");
        Map<String, String> header = new HashMap<>();
        JSONObject jBody = apiGetCall(bitcoinUrl, header);
        String currentRate = jBody.getJSONObject("bpi").getJSONObject("USD").getString("rate");
        return "*-----$BTC$-----*\n" + "BitCoin Price: *$" + currentRate + "*\nGrab it before its too late.";
    }

    public String getChuckJoke(String[] inputName) throws InterruptedException {
        logger.info("Inside Chuck Joke Method-------------------------------------");
        Map<String, String> header = new HashMap<>();
        header.put("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        String[] name = inputName;
        if (inputName == null) {
            name = getRandomName();
        }
        String finalUrl = chuckUrl + "?firstName=" + name[0] + "&lastName=" + name[1];
        JSONObject jBody = apiGetCall(finalUrl, header);
        logger.info(chuckUrl);
        String joke = jBody.getJSONObject("value").getString("joke");
        return joke + "\n:wine_glass:";
    }

}

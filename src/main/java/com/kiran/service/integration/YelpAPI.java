package com.kiran.service.integration;

import com.kiran.service.exception.InvalidMove;
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

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * @author Kiran
 * @since 9/3/17
 */

@Component
public class YelpAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${yelp.search.url}")
    private String yelpSearchURL;
    @Value("${yelp.auth}")
    private String yelpAuth;


    public HashMap<Integer, HashMap<String, String>> findRestaurants(String restaurant, String location) throws InterruptedException {
        String termFilter = "?term="+restaurant;
        String locationFilter = "&location="+location;
        String sortByFilter = "&sort_by=review_count";
        String URL = yelpSearchURL + termFilter+locationFilter;

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", yelpAuth);
            HttpEntity<String> request = new HttpEntity<String>(headers);
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, request, String.class);
            JSONObject jObject = new JSONObject(response.getBody());
            JSONArray jArray = jObject.getJSONArray("businesses");
            HashMap<Integer, HashMap<String, String>> restaurantsInfo = new HashMap<>();

            DecimalFormat df2 = new DecimalFormat(".##");
            for (int i = 0; i < 5; i++) {
                HashMap<String, String> oneRestaurantInfo = new HashMap<>();
                oneRestaurantInfo.put("name", jArray.getJSONObject(i).getString("name"));
                oneRestaurantInfo.put("url", jArray.getJSONObject(i).getString("url"));
                oneRestaurantInfo.put("review", Integer.toString(jArray.getJSONObject(i).getInt("review_count")));
                oneRestaurantInfo.put("rating", Double.toString(jArray.getJSONObject(i).getDouble("rating")));
                oneRestaurantInfo.put("image_url", jArray.getJSONObject(i).getString("image_url"));
                oneRestaurantInfo.put("location", jArray.getJSONObject(i).getJSONObject("location").getString("city"));
                long distanceMeter = jArray.getJSONObject(i).getLong("distance");
                double inches = (39.370078 * distanceMeter);
                double miles = (inches / 63360.00);
                oneRestaurantInfo.put("distance", df2.format(miles));
                restaurantsInfo.put(i, oneRestaurantInfo);
            }
            return restaurantsInfo;
        } catch (Exception ex) {
            this.logger.error("Exception during Yelp API call:"+URL+" ExceptionMessage=\'{}\'. StackTrace=\'{}\'", ex.getMessage(), ex.getStackTrace());
            throw new InvalidMove("Something went wrong, please contact your administrator.");
        }
    }
}

package com.kiran.service.integration;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kiran
 * @since 10/5/17
 */

@Component
public class SlackAPI extends BaseApiCall{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${slack.url.private.channel}")
    private String slackPrivateUrl;
    @Value("${slack.url.public.channel}")
    private String slackPublicUrl;
    @Value("${slack.url.user.info}")
    private String slackUserInfo;
    @Value("${slack.token}")
    private String slackToken;

    public ArrayList getUsersFromPrivateGroup(String channelId) throws InterruptedException {
        String token = "token="+ slackToken;
        String channel = "&channel="+ channelId;
        String url = slackPrivateUrl + token + channel;

        Map<String, String> header = new HashMap<>();
        JSONObject jBody = apiGetCall(url, header);
        ArrayList userIds  = (ArrayList) jBody.getJSONObject("group").getJSONArray("members").toList();
        ArrayList userNames = getUserName(userIds);
        return userNames;
    }

    public ArrayList getUsersFromPublicGroup(String channelId) throws InterruptedException {
        String token = "token="+ slackToken;
        String channel = "&channel="+ channelId;
        String url = slackPublicUrl + token + channel;

        Map<String, String> header = new HashMap<>();
        JSONObject jBody = apiGetCall(url, header);
        ArrayList userIds  = (ArrayList) jBody.getJSONObject("channel").getJSONArray("members").toList();
        ArrayList userNames = getUserName(userIds);
        return userNames;
    }


    private ArrayList getUserName(ArrayList<String> userIds) throws InterruptedException {
        ArrayList<String> users = new ArrayList<>();
        for (String userId : userIds) {
            String token = "token="+ slackToken;
            String user = "&user="+ userId;
            String url = slackUserInfo + token + user;

            Map<String, String> header = new HashMap<>();
            JSONObject jBody = apiGetCall(url, header);
            String userName  =  jBody.getJSONObject("user").getJSONObject("profile").getString("display_name");
            users.add(userName);
        }
        return users;
    }

}

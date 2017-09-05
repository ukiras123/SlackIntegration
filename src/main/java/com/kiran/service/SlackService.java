package com.kiran.service;

import com.kiran.controller.dto.Slack.SlackAttachment;
import com.kiran.controller.dto.Slack.SlackAttachmentFields;
import com.kiran.controller.dto.Slack.SlackResponseAttachment;
import com.kiran.service.exception.InvalidMove;
import com.kiran.service.integration.JiraAPI;
import com.kiran.service.integration.WitAPI;
import com.kiran.service.integration.YelpAPI;
import com.kiran.service.utilities.Utilities;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Kiran
 * @since 8/31/17
 */
@Component
public class SlackService {

    @Autowired
    private JiraAPI jiraAPI;

    @Autowired
    private WitAPI witAPI;

    @Autowired
    private YelpAPI yelpAPI;

    public HashMap  getJiraResponse(String jiraTicket)
    {
        HashMap<String, String> hmap = new HashMap<String, String>();
        try {
            JSONObject k = jiraAPI.getTicketDetail(jiraTicket);
            String summary = k.getJSONObject("fields").getString("summary");
            String asignee = "N/A";
            if (!k.getJSONObject("fields").get("assignee").toString().equals("null")) {
                asignee  = k.getJSONObject("fields").getJSONObject("assignee").getString("name");
            }
            String status = k.getJSONObject("fields").getJSONObject("status").getString("name");
            hmap.put("summary", summary);
            hmap.put("assignee", asignee);
            hmap.put("status", status);
        } catch (InvalidMove b) {
            throw b;
        }catch (Exception ex) {
            throw new InvalidMove(ex.getMessage());
        }
        return hmap;
    }

    public String getJiraCode(String str) {
        String part[] = str.split(" ");
        LinkedList<String> mayBeJira = new LinkedList<>();
        String jiraTicket = null;
        for (String st : part) {
            if (st.length()==9 || st.length()==10 || st.length()==11)
            {
                mayBeJira.add(st);
            }
        }
        for (int i = 0; i < mayBeJira.size(); i++) {
            if (mayBeJira.get(i).contains("-") && mayBeJira.get(i).length() == 9) {
                jiraTicket = mayBeJira.get(i);
                break;
            } else if (mayBeJira.get(i).contains("-") && mayBeJira.get(i).length() == 10) {
                String st = mayBeJira.get(i);
                jiraTicket = st.substring(0, st.length() - 1);
                break;
            } else if (mayBeJira.get(i).contains("-") && mayBeJira.get(i).length() == 11) {
                String st = mayBeJira.get(i);
                jiraTicket = st.substring(0, st.length() - 2);
                break;
            }
        }
        return jiraTicket;
    }

    public String getAssigneeName(String str) {
        String part[] = str.split(" ");
        String mayBeUser = null;
        String asignee = null;
        for (String st : part) {
            if (st.contains("user"))
            {
                mayBeUser = st;
                break;
            }
        }
        if (mayBeUser != null) {
            if (mayBeUser.contains(":")) {
                asignee = mayBeUser.substring(mayBeUser.indexOf(":") + 1);
            }
        }
        return asignee;
    }


    public HashMap<Integer, HashMap<String, String>>  get_restaurant_list(String userInput)
    {
        String restaurant = "Best Restaurants";
        String location = "";
        try {
            HashMap<String, String> entities = witAPI.understandMe(userInput);
            if (!entities.containsKey(Utilities.WIT_ENTITIES.FOOD.getName()) && !entities.containsKey(Utilities.WIT_ENTITIES.LOCATION.getName())) {
                throw new InvalidMove("*At least input location.*");
            }

            if (entities.containsKey(Utilities.WIT_ENTITIES.FOOD.getName())) {
                restaurant = entities.get(Utilities.WIT_ENTITIES.FOOD.getName());
            }

            if (!entities.containsKey(Utilities.WIT_ENTITIES.LOCATION.getName())) {
                throw new InvalidMove("*I need location to find the restaurants. Please try again.*");
            } else {
                    location = entities.get(Utilities.WIT_ENTITIES.LOCATION.getName());
            }
            HashMap<Integer, HashMap<String, String>> restaurantsInfo = yelpAPI.findRestaurants(restaurant, location);
            return restaurantsInfo;
        } catch (InvalidMove e) {
            throw new InvalidMove(e.getError_message());
        }catch (Exception e) {
            throw new InvalidMove("*I didn't understand what you said. Please try again.*");
        }
    }

    public SlackResponseAttachment createSlackResponse(HashMap<Integer, HashMap<String, String>> restaurants) {
        List<SlackAttachmentFields> fields = new LinkedList<>();
        for (int i =0;i < restaurants.size();i++) {
            fields.add(new SlackAttachmentFields(restaurants.get(i).get("name").toString(), restaurants.get(i).get("rating").toString()+ " Stars, "+restaurants.get(i).get("review").toString()+" Ratings, "+restaurants.get(i).get("distance").toString()+" miles away",true));
        }
        List<SlackAttachment> slackAttachments = new LinkedList<>();
        slackAttachments.add(new SlackAttachment("If you are interested in my choice", restaurants.get(0).get("url").toString(), "Top 5 Restaurants around "+restaurants.get(0).get("location").toString(),  restaurants.get(0).get("image_url").toString(), fields));
        SlackResponseAttachment responseAttachment = new SlackResponseAttachment(null, slackAttachments);
        return responseAttachment;
    }

}

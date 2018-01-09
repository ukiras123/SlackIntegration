package com.kiran.service;

import com.kiran.controller.dto.SlackDTO.*;
import com.kiran.service.exception.InvalidMove;
import com.kiran.service.integration.JiraAPI;
import com.kiran.service.integration.WitAPI;
import com.kiran.service.integration.YelpAPI;
import com.kiran.service.utilities.Utilities;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Kiran
 * @since 8/31/17
 */

@Service
public class SlackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JiraAPI jiraAPI;

    @Autowired
    private WitAPI witAPI;

    @Autowired
    private YelpAPI yelpAPI;

    @Autowired
    private Utilities utilities;

    public HashMap  getJiraResponse(String jiraTicket)
    {
        HashMap<String, String> hmap = new HashMap<String, String>();
        try {
            JSONObject k = jiraAPI.getTicketDetail(jiraTicket);
            String summary = k.getJSONObject("fields").getString("summary");
            String asignee = "N/A";
            if (!k.getJSONObject("fields").get("assignee").toString().equals("null")) {
                asignee  = k.getJSONObject("fields").getJSONObject("assignee").getString("displayName");
            }
            String status = k.getJSONObject("fields").getJSONObject("status").getString("name");
            JSONArray commentArray = k.getJSONObject("fields").getJSONObject("comment").getJSONArray("comments");
            String comment = null;
            if (commentArray.length() != 0) {
                comment = commentArray.getJSONObject(commentArray.length()-1).getJSONObject("updateAuthor").getString("displayName");
                comment += "-> "+ commentArray.getJSONObject(commentArray.length()-1).getString("body");
            }
            hmap.put("summary", summary);
            hmap.put("assignee", asignee);
            hmap.put("status", status);
            hmap.put("lastComment", comment);
        } catch (InvalidMove b) {
            throw b;
        }catch (Exception ex) {
            this.logger.error("Exception during getJiraResponse(). ExceptionMessage=\'{}\'. StackTrace=\'{}\'", ex.getMessage(), ex.getStackTrace());
            throw new InvalidMove(ex.getMessage());
        }
        return hmap;
    }

    public String getJiraCode(String str) {
        String part[] = str.split(" ");
        String jiraTicket = null;
        for (String st : part) {
            if (st.contains("-"))
            {
                jiraTicket = st;
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
        }catch (Exception ex) {
            this.logger.error("Exception during get_restaurant_list. ExceptionMessage=\'{}\'. StackTrace=\'{}\'", ex.getMessage(), ex.getStackTrace());
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


    public SlackResponseAttachment createSlackResponseDictionaryYesNo(String text) {
        SlackAction action1 = new SlackAction("Yes", "button", "primary", "yes");
        SlackAction action2 = new SlackAction("No", "button", "danger", "no");
        List<SlackAction> actions = new LinkedList<>();
        actions.add(action1);
        actions.add(action2);
        SlackInteractiveAttachment attachment = new SlackInteractiveAttachment("Do you want to get a sentence for this word?","You are unable to choose Yes or No","dictionary", actions);
        List<SlackInteractiveAttachment> attachments = new LinkedList<>();
        attachments.add(attachment);
        SlackResponseAttachment responseAttachment = new SlackResponseAttachment(text, attachments);
        return responseAttachment;
    }

    public SlackResponseAttachment createSlackResponseSprintYesNo(String text, String callBack) {
        SlackAction action1 = new SlackAction("Yes", "button", "primary", "yes");
        SlackAction action2 = new SlackAction("No", "button", "danger", "no");
        List<SlackAction> actions = new LinkedList<>();
        actions.add(action1);
        actions.add(action2);
        SlackInteractiveAttachment attachment = new SlackInteractiveAttachment("Do you want to see the ticket's summary?","You are unable to choose Yes or No",callBack, actions);
        List<SlackInteractiveAttachment> attachments = new LinkedList<>();
        attachments.add(attachment);
        SlackResponseAttachment responseAttachment = new SlackResponseAttachment(text, attachments);
        return responseAttachment;
    }

    public SlackResponseAttachment createDuckVote(String text, String callBack) {
        SlackAction action1 = new SlackAction("Support", "button", "primary", "yes");
        SlackAction action2 = new SlackAction("Do not support", "button", "danger", "no");
        List<SlackAction> actions = new LinkedList<>();
        actions.add(action1);
        actions.add(action2);
        SlackInteractiveAttachment attachment = new SlackInteractiveAttachment("You need at least three votes from your team to steal the duck.","You are unable to choose Yes or No",callBack, actions);
        List<SlackInteractiveAttachment> attachments = new LinkedList<>();
        attachments.add(attachment);
        SlackResponseAttachment responseAttachment = new SlackResponseAttachment(text, attachments);
        return responseAttachment;
    }

    public SlackResponseAttachment createTrivia(String user, JSONObject jBody, String callBack) {
        String question = jBody.getJSONArray("results").getJSONObject(0).getString("question");
        String correctAnswer = jBody.getJSONArray("results").getJSONObject(0).getString("correct_answer");
        String incorrectAnswer1 = jBody.getJSONArray("results").getJSONObject(0).getJSONArray("incorrect_answers").get(0).toString();
        String incorrectAnswer2 = jBody.getJSONArray("results").getJSONObject(0).getJSONArray("incorrect_answers").get(1).toString();
        String incorrectAnswer3 = jBody.getJSONArray("results").getJSONObject(0).getJSONArray("incorrect_answers").get(2).toString();
        String choices[] = {correctAnswer, incorrectAnswer1, incorrectAnswer2, incorrectAnswer3};
        utilities.shuffleArray(choices);
        List<SlackAction> actions = new LinkedList<>();
        for (String k: choices) {
            if (k.equalsIgnoreCase(correctAnswer)) {
                SlackAction action = new SlackAction(k, "button", "primary", "yes");
                actions.add(action);
            } else {
                SlackAction action = new SlackAction(k, "button", "primary", "no");
                actions.add(action);
            }
        }
        SlackInteractiveAttachment attachment = new SlackInteractiveAttachment("You will get a new duck for a correct answer and lose onc if its wrong.","You are unable to choose Yes or No",callBack, actions);
        List<SlackInteractiveAttachment> attachments = new LinkedList<>();
        attachments.add(attachment);
        SlackResponseAttachment responseAttachment = new SlackResponseAttachment("*"+question+"*", attachments);
        return responseAttachment;
    }

    public SlackResponseAttachment createTriviaChoice(String user, String callBack) {
        List<SlackAction> actions = new LinkedList<>();
        List<Utilities.TRIVIA_CATEGORIES> triviaCategories = Arrays.asList(Utilities.TRIVIA_CATEGORIES.values());

        for (Utilities.TRIVIA_CATEGORIES category: triviaCategories) {
                SlackAction action = new SlackAction(category.getName(), "button", "primary", Integer.toString(category.getId()));
                actions.add(action);
        }

        SlackInteractiveAttachment attachment = new SlackInteractiveAttachment("Select a Category.","You are unable to choose any",callBack, actions);
        List<SlackInteractiveAttachment> attachments = new LinkedList<>();
        attachments.add(attachment);
        SlackResponseAttachment responseAttachment = new SlackResponseAttachment("*<@"+user+">, you only have once choice, make it the best choice.*", attachments);
        return responseAttachment;
    }
}

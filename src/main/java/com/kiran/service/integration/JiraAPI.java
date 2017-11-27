package com.kiran.service.integration;

import com.kiran.controller.dto.SlackDTO.SprintTicket;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Kiran
 * @since 8/31/17
 */

@Component
public class JiraAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${jira.url}")
    private String jiraUrl;
    @Value("${jira.user}")
    private String jiraUser;
    @Value("${jira.pass}")
    private String jiraPass;

    private String getBasicAuth() {
        String plainCreds = jiraUser + ":" + jiraPass;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        return base64Creds;
    }

    public JSONObject getTicketDetail(String jiraTicket) throws InterruptedException {
        String URL = jiraUrl + "/issue/" + jiraTicket + ".json";

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + getBasicAuth());
            HttpEntity<String> request = new HttpEntity<String>(headers);
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, request, String.class);
            JSONObject jObject = new JSONObject(response);
            String strBody = jObject.getString("body");
            JSONObject jBody = new JSONObject(strBody);
            return jBody;
        } catch (Exception ex) {
            this.logger.error("Exception during Jira API call. ExceptionMessage=\'{}\'. StackTrace=\'{}\'", ex.getMessage(), ex.getStackTrace());
        }
        return null;
    }

    public String getArgoSprintDetail(boolean showAll) throws InterruptedException {
        logger.info("inside getArgoSprintDetail ---------");
        String URL = jiraUrl + "/search";
        String payload = "{\"jql\":\"project = ARGO AND issuetype in (Bug, Story, Task) AND \\\"Epic Link\\\" = ARGO-1910 and sprint in openSprints() ORDER BY status \",\"fields\":[\"summary\",\"status\",\"assignee\",\"customfield_10008\"]}";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + getBasicAuth());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(payload, headers);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, request, String.class);
        JSONObject jObject = new JSONObject(response);
        String strBody = jObject.getString("body");
        JSONObject jBody = new JSONObject(strBody);
        int totalTickets = jBody.getInt("total");
        int doneTickets = 0;
        JSONArray jsonArray = jBody.getJSONArray("issues");
        List<SprintTicket> ticketList = new LinkedList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            HashMap<String, String> ticketDetail = new HashMap<>();
            String keyValue = jsonArray.getJSONObject(i).getString("key");
            String summaryValue = jsonArray.getJSONObject(i).getJSONObject("fields").getString("summary");
            String assigneeNameValue = "N/A";
            if (!jsonArray.getJSONObject(i).getJSONObject("fields").isNull("assignee")) {
                assigneeNameValue = jsonArray.getJSONObject(i).getJSONObject("fields").getJSONObject("assignee").getString("displayName");
            }
            String statusValue = jsonArray.getJSONObject(i).getJSONObject("fields").getJSONObject("status").getString("name");
            if (statusValue.equalsIgnoreCase("DONE")) {
                doneTickets++;
            }
            SprintTicket tempTicket = new SprintTicket(keyValue, summaryValue, assigneeNameValue, statusValue);
            ticketList.add(tempTicket);
        }
        Collections.sort(ticketList);
        String finalString = "*Welcome to ARGO*\n";
        finalString += "*---------- Total: " + totalTickets + ", Done: "+doneTickets+" ----------*\n";
        if (showAll == false) {
            for (SprintTicket ticket : ticketList) {
                finalString += "*" + ticket.getTicket() + "*   *\"" + ticket.getStatus() + "\"*   *" + ticket.getAssigneeName() + "*     \n";
            }
        } else {
            for (SprintTicket ticket : ticketList) {
                finalString += "*" + ticket.getTicket() + "*   *\"" + ticket.getStatus() + "\"*   *" + ticket.getAssigneeName() + "*     \n     "+ ticket.getSummary()+"\n";
            }
        }
        return finalString;
    }


    public String assignATicket(String jiraTicket, String asignee) throws InterruptedException {
        String URL = jiraUrl + "/issue/" + jiraTicket + "/assignee";
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + getBasicAuth());
            headers.setContentType(MediaType.APPLICATION_JSON);
            String input = "{\"name\":\"" + asignee + "\"}";
            HttpEntity<String> entity = new HttpEntity<String>(input, headers);
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class);
            HttpStatus statusCode = response.getStatusCode();
            int code = statusCode.value();
            if (code == 204) {
                return "passed";
            } else {
                return "failed";
            }
        } catch (HttpClientErrorException ex) {
            System.out.println("** exception: " + ex.getMessage());
            if (ex.getStatusCode().value() == 400) {
                return "userIssue";
            } else if (ex.getStatusCode().value() == 404) {
                return "jiraTicket";
            } else {
                return "failed";
            }
        } catch (Exception ex) {
            this.logger.error("Exception during Jira API , assign ticker. ExceptionMessage=\'{}\'. StackTrace=\'{}\'", ex.getMessage(), ex.getStackTrace());
            return "failed";
        }
    }


}

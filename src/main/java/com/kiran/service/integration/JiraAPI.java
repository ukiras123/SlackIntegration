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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String jiraTicketUrl = "https://jira.oceanx.com/browse/";
        String payload = "{\"jql\":\"project = ARGO AND issuetype in (Bug, Story, Task) AND \\\"Epic Link\\\" = ARGO-1910 and sprint in openSprints() ORDER BY status \",\"fields\":[\"summary\",\"status\",\"assignee\",\"customfield_10008\",\"customfield_10000\"]}";
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
        String sprintEndDate = "";
        String daysLeft = "";
        JSONArray jsonArray = jBody.getJSONArray("issues");
        List<SprintTicket> ticketList = new LinkedList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            if (i == 0) {
                sprintEndDate = jsonArray.getJSONObject(i).getJSONObject("fields").getJSONArray("customfield_10000").get(0).toString();
                daysLeft = getJiraDaysLeft(sprintEndDate);
            }
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
        String finalString = ">*---------- Welcome to ARGO ---------- *\n";
        finalString += ">* Total: " + totalTickets + ", Done: " + doneTickets + ", `" + daysLeft + "` to go." + " ---------- *\n";
        if (showAll == false) {
            for (SprintTicket ticket : ticketList) {
                String symbol = getSymbol(ticket.getStatus());
                String ticketCode = ticket.getTicket();
                finalString += "*" + createHyperlink(jiraTicketUrl + ticketCode, ticketCode) + "*   *\"" + ticket.getStatus() + "\"*   *" + ticket.getAssigneeName() + "*     " + symbol + "\n";
            }
        } else {
            for (SprintTicket ticket : ticketList) {
                String symbol = getSymbol(ticket.getStatus());
                String ticketCode = ticket.getTicket();
                finalString += "*" + createHyperlink(jiraTicketUrl + ticketCode, ticketCode) + "*   *\"" + ticket.getStatus() + "\"*   *" + ticket.getAssigneeName() + "*     " + symbol + "\n     " + ticket.getSummary() + "\n";
            }
        }
        return finalString + getMessage(totalTickets, doneTickets);
    }


    private String getSymbol(String status) {
        String symbol = "";
        if (status.equalsIgnoreCase("Done")) {
            symbol = ":white_check_mark:";
        } else if (status.equalsIgnoreCase("In Backlog")) {
            symbol = ":interrobang:";
        } else if (status.equalsIgnoreCase("In QA")) {
            symbol = ":sonic:";
        } else if (status.equalsIgnoreCase("In Progress") || status.equalsIgnoreCase("In Development")) {
            symbol = ":ninja:";
        } else if (status.equalsIgnoreCase("In Review")) {
            symbol = ":time:";
        } else if (status.equalsIgnoreCase("Analysis") || status.equalsIgnoreCase("Ready for Dev") || status.equalsIgnoreCase("To Do")) {
            symbol = ":exclamation:";
        }
        return symbol;
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

    private String createHyperlink(String url, String hyperlinkText) {
        return "<" + url + "|" + hyperlinkText + ">";
    }

    private String getMessage(int total, int completed) {
        int diff = total - completed;
        if (diff == 0) {
            return "`Congratulations!!! That was a clean sprint. :tada: :confetti_ball:`";
        } else if (diff == 1) {
            return "`One more push and we will get there! Come one team.`";
        } else if (diff == 2) {
            return "`We did a lot, lets just get these two tickets done and we will reach our goal.`";
        } else if (diff == 3) {
            return "`Guys, lets not slack any more, finish these three tickets.`";
        } else {
            return "";
        }
    }

    private String getJiraDaysLeft(String jiraString) {
        Date extractedDate = null;
        try {
            extractedDate = extractDate(jiraString, "(?<=API- )(.*)(?=,start)");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long dateDiff = getDateDiff(new Date(), extractedDate, TimeUnit.DAYS);
        return dateDiff + " days";
    }

    private Date extractDate(String text, String regex) throws ParseException {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        String date = "";
        while (m.find()) {
            date = m.group();
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = format.parse(date);
        return d;
    }

    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

}

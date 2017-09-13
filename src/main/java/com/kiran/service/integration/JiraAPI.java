package com.kiran.service.integration;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author Kiran
 * @since 8/31/17
 */

@Component
public class JiraAPI {

    @Value("${jira.url}")
    private String jiraUrl;
    @Value("${jira.user}")
    private String jiraUser;
    @Value("${jira.pass}")
    private String jiraPass;

    private String getBasicAuth() {
        String plainCreds = jiraUser+":"+jiraPass;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        return base64Creds;
    }

    public JSONObject getTicketDetail(String jiraTicket) throws InterruptedException {
        String URL = jiraUrl+jiraTicket+".json";

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + getBasicAuth());
            HttpEntity<String> request = new HttpEntity<String>(headers);
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, request, String.class);
            JSONObject jObject  = new JSONObject(response);
            String strBody =   jObject.getString("body");
            JSONObject jBody  = new JSONObject(strBody);
            return jBody;
        } catch (Exception ex) {
            System.out.println("** exception: " + ex.getMessage());
        }
        return null;
    }


    public String assignATicket(String jiraTicket, String asignee) throws InterruptedException {
        String URL = jiraUrl+jiraTicket+"/assignee";
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
        }catch(HttpClientErrorException ex)
        {
            System.out.println("** exception: " + ex.getMessage());
            if (ex.getStatusCode().value() == 400) {
                return "userIssue";
            } else if (ex.getStatusCode().value() == 404) {
            return "jiraTicket";
            } else {
                return "failed";
            }
        } catch (Exception ex) {
                return "failed";
        }
    }


}

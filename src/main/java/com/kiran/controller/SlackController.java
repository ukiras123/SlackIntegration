package com.kiran.controller;

import com.kiran.controller.dto.RetroDTO.RetroDTO;
import com.kiran.controller.dto.SlackDTO.SlackResponseAttachment;
import com.kiran.model.entity.RetroEntity;
import com.kiran.model.response.ReadAllRetroResponse;
import com.kiran.model.response.SlackResponse;
import com.kiran.service.DuckService;
import com.kiran.service.RetroService;
import com.kiran.service.SlackService;
import com.kiran.service.exception.InvalidMove;
import com.kiran.service.integration.DictionaryAPI;
import com.kiran.service.integration.JiraAPI;
import com.kiran.service.integration.RandomAPI;
import com.kiran.service.integration.TriviaAPI;
import com.kiran.service.utilities.SlackAsyncService;
import com.kiran.service.utilities.Utilities;
import com.kiran.translator.RetroTranslator;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Kiran
 * @since 8/24/17
 */

@RestController
@RequestMapping("/slack")
public class SlackController {

    @Autowired
    private Utilities utilities;

    @Autowired
    private SlackService slackService;

    @Autowired
    private DuckService duckService;

    @Autowired
    private JiraAPI jiraAPI;

    @Autowired
    private DictionaryAPI dictionaryAPI;

    @Autowired
    private RandomAPI randomAPI;

    @Autowired
    private TriviaAPI triviaAPI;

    @Autowired
    private SlackAsyncService slackAsyncService;


    @Autowired
    private RetroService retroService;

    @Autowired
    private RetroTranslator retroTranslator;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Health Check
    @RequestMapping(method = RequestMethod.GET)
    public String healthCheck() {
        return "<marquee behavior=\"alternate\" scrollamount=\"5\">I...</marquee>\n" +
                "<marquee behavior=\"alternate\" scrollamount=\"6\">AM...</marquee>\n" +
                "<marquee behavior=\"alternate\" scrollamount=\"7\">WORKING...</marquee>\n" +
                "<marquee behavior=\"alternate\" scrollamount=\"8\">FINE!</marquee>\n" +
                "\n";
    }


    //Slack================================

    @RequestMapping(value = "/jira/issue", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<?> getIssueDetail(@RequestBody MultiValueMap<String, String> formVars) {
        logger.info("Inside /jira/issue controller------------------------------------");
        try {
            String userName = utilities.trimString(formVars.get("user_name").toString(), 1);
            String text = utilities.trimString(formVars.get("text").toString(), 1);
            slackAsyncService.logInDB(userName, text);
            if (!text.isEmpty()) {
                String jiraTicket = slackService.getJiraCode(text);
                if (jiraTicket == null) {
                    SlackResponse response = new SlackResponse("Please input a valid Jira Ticket");
                    return new ResponseEntity<>(response, null, HttpStatus.OK);
                }
                HashMap<String, String> jiraMap;
                jiraMap = slackService.getJiraResponse(jiraTicket);
                String responseString = "*Ticket* : " + jiraTicket + "\n*Summary* : " + jiraMap.get("summary") + "\n*Assignee* : " + jiraMap.get("assignee") + "\n*Status* : " + jiraMap.get("status");
                if (jiraMap.get("lastComment") != null) {
                    responseString += "\n*LastComment* : " + jiraMap.get("lastComment");
                }
                SlackResponse response = new SlackResponse(responseString);
                return new ResponseEntity<>(response, null, HttpStatus.OK);

            } else {
                SlackResponse response = new SlackResponse("Welcome, " + userName.substring(0, 1).toUpperCase() + userName.substring(1) + ". You can now look for Jira Ticket Info.");
                return new ResponseEntity<>(response, null, HttpStatus.OK);
            }
        } catch (InvalidMove e) {
            SlackResponse response = new SlackResponse("Something went wrong. Please try again");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Please contact your administrator", null, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Exiting /jira/issue controller------------------------------------");
        }
    }


    @RequestMapping(value = "/jira/assignee", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<?> assignTicket(@RequestBody MultiValueMap<String, String> formVars) {
        logger.info("Inside /jira/assignee controller------------------------------------");
        try {
            String userName = utilities.trimString(formVars.get("user_name").toString(), 1);
            String text = utilities.trimString(formVars.get("text").toString(), 1);
            slackAsyncService.logInDB(userName, text);
            if (!text.isEmpty()) {
                String jiraTicket = slackService.getJiraCode(text);
                String assigneeName = slackService.getAssigneeName(text);
                if (jiraTicket == null) {
                    SlackResponse response = new SlackResponse("Please input a valid Jira Ticket");
                    return new ResponseEntity<>(response, null, HttpStatus.OK);
                }
                if (assigneeName == null) {
                    SlackResponse response = new SlackResponse("Format not correct.\nUser \"XXXX-3333 to user:JiraUserName\"");
                    return new ResponseEntity<>(response, null, HttpStatus.OK);
                }
                String passed = jiraAPI.assignATicket(jiraTicket, assigneeName);
                if (passed.equals("passed")) {
                    SlackResponse response = new SlackResponse("Ticket: *" + jiraTicket + "* is assigned to *" + assigneeName + "*");
                    return new ResponseEntity<>(response, null, HttpStatus.OK);
                } else if (passed.equals("userIssue")) {
                    SlackResponse response = new SlackResponse("User: *" + assigneeName + "* does not exist.");
                    return new ResponseEntity<>(response, null, HttpStatus.OK);
                } else if (passed.equals("jiraTicket")) {
                    SlackResponse response = new SlackResponse("Jira Ticket is invalid");
                    return new ResponseEntity<>(response, null, HttpStatus.OK);
                } else {
                    SlackResponse response = new SlackResponse("Something went wrong. Please try again.");
                    return new ResponseEntity<>(response, null, HttpStatus.OK);
                }

            } else {
                SlackResponse response = new SlackResponse("Welcome, " + userName.substring(0, 1).toUpperCase() + userName.substring(1) + ". You can now Assign a Jira Ticket");
                return new ResponseEntity<>(response, null, HttpStatus.OK);
            }
        } catch (InvalidMove e) {
            SlackResponse response = new SlackResponse("Something went wrong. Please try again");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            SlackResponse response = new SlackResponse("Something went wrong. Please contact your administrator.");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } finally {
            logger.info("Exiting /jira/assignee controller------------------------------------");
        }
    }

    @RequestMapping(value = "/food", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<?> getRestaurantsDetails(@RequestBody MultiValueMap<String, String> formVars) throws InterruptedException {
        logger.info("Inside /food controller------------------------------------");
        try {
            String userName = utilities.trimString(formVars.get("user_name").toString(), 1);
            String text = utilities.trimString(formVars.get("text").toString(), 1);
            String responseUrl = utilities.trimString(formVars.get("response_url").toString(), 1);
            SlackResponse responseOk = new SlackResponse("Searching...");
            slackAsyncService.logInDB(userName, text);
            slackAsyncService.postMessage(userName, text, responseUrl);
            return new ResponseEntity<>(responseOk, null, HttpStatus.OK);
        } finally {
            logger.info("Exiting /food controller------------------------------------");
        }
    }


    @RequestMapping(value = "/regression", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<?> runRegression(@RequestBody MultiValueMap<String, String> formVars) throws InterruptedException {
        logger.info("Inside /regression controller------------------------------------");
        try {
            String userName = utilities.trimString(formVars.get("user_name").toString(), 1);
            String text = utilities.trimString(formVars.get("text").toString(), 1);
            String[] splited = text.split("\\s+");
            String responseUrl = utilities.trimString(formVars.get("response_url").toString(), 1);
            SlackResponse responseOk = new SlackResponse("Testing " + splited[0] + "...... Branch: " + splited[1]);
            slackAsyncService.logInDB(userName, text);
            slackAsyncService.regressionTestResponse(splited[0], splited[1], splited[2], responseUrl);
            return new ResponseEntity<>(responseOk, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong. Please contact your administrator.");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } finally {
            logger.info("Exiting /regression controller------------------------------------");
        }
    }


    //Read all message
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> readRetros() {
        Iterable<RetroEntity> retroEntityIterable = retroService.readAllActiveRetro();
        List<RetroDTO> retroEntityList =
                retroTranslator.entityListToDTOList(retroEntityIterable);
        ReadAllRetroResponse response = new ReadAllRetroResponse(retroEntityList);
        return new ResponseEntity<>(response, null, HttpStatus.OK);
    }


    //CreateRetro
    @RequestMapping(value = "/todo", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createRetro(@RequestBody MultiValueMap<String, String> formVars) {
        try {
            String userName = utilities.trimString(formVars.get("user_name").toString(), 1);
            String text = utilities.trimString(formVars.get("text").toString(), 1);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String timeStamp = dateFormat.format(date);
            RetroEntity entity = new RetroEntity(userName, text, timeStamp, true);
            slackAsyncService.logInDB(userName, text);
            retroService.createRetro(entity);
            SlackResponse response = new SlackResponse("Your message has been saved to the list.", true);
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong. Please contact your administrator.", true);
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }
    }

    //Clear all message
    @RequestMapping(value = "/todo/clear", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> clearRetros(@RequestBody MultiValueMap<String, String> formVars) {
        try {
            String userName = utilities.trimString(formVars.get("user_name").toString(), 1);
            String text = utilities.trimString(formVars.get("text").toString(), 1);
            slackAsyncService.logInDB(userName, text);
            String message = "";
            Iterable<RetroEntity> retroEntityIterable = retroService.readAllActiveRetro(userName);
            for (RetroEntity e : retroEntityIterable) {
                e.setActive(false);
                retroService.createRetro(e);
            }
            message = "All previous To-Do list has been cleared.";
            SlackResponse response = new SlackResponse(message, true);
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong. Please contact your administrator.", true);
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }

    }

    //Read all message
    @RequestMapping(value = "/todo/read", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> readRetros(@RequestBody MultiValueMap<String, String> formVars) {
        try {
            String userName = utilities.trimString(formVars.get("user_name").toString(), 1);
            List<RetroEntity> retroEntityList = retroService.readAllActiveRetro(userName);
            String message = "";

            if (retroEntityList.size() == 0) {
                message = "\n*Your To-Do list is empty.*\nTo add, just type /add-to-my-todo [message]";
            } else {
                message = "\n*-------" + userName.substring(0, 1).toUpperCase() + userName.substring(1) + "'s To-Do List-------*\n";
                int i = 1;
                for (RetroEntity e : retroEntityList) {
                    message += "*" + i + ". " + e.getRetroMessage() + "*\n";
                    i++;
                }
            }
            String finalMessage = utilities.trimString(message, 1);
            SlackResponse response = new SlackResponse(finalMessage);
            if (retroEntityList.size() == 0) {
                response.setResponse_type("private");
            }
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong. Please contact your administrator.", true);
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }

    }


    @RequestMapping(value = "/dictionary/read", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getMeaning(@RequestBody MultiValueMap<String, String> formVars) {
        String userName = utilities.trimString(formVars.get("user_name").toString(), 1);
        String text = utilities.trimString(formVars.get("text").toString(), 1);
        try {
            String meaning = dictionaryAPI.getMeaning(text);
            //+ "\n*Sentence:* " + sentence+"\n:thinking_face::beer::sunglasses::the_horns:"
            String responseTest = "*--------------- " + text.toUpperCase() + " ---------------*" + "\n*Meaning:* " + meaning;
            SlackResponseAttachment response = slackService.createSlackResponseDictionaryYesNo(responseTest);
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Sorry, is \"" + text + "\" even a word?");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/surprise", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getSurprise(@RequestBody MultiValueMap<String, String> formVars) {
        String outCome = "";
        try {
            outCome = randomAPI.getSurprise();
            SlackResponse response = new SlackResponse(outCome);
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong, please try again.");
            response.setResponse_type("private");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/sarcasm", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getSarcasm(@RequestBody MultiValueMap<String, String> formVars) {
        try {
            String userName = utilities.trimString(formVars.get("user_name").toString(), 1);
            String text = utilities.trimString(formVars.get("text").toString(), 1);
            String[] parts = null;
            if (text.length() > 2) {
                parts = text.split(" ");
            }
            String category = null;
            if (parts.length >= 3) {
                category = parts[2];
            }
            logger.info(text);
            String sarcasm = randomAPI.getChuckJoke(parts, category);
            SlackResponse response = new SlackResponse(sarcasm);
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong, please try again.");
            response.setResponse_type("private");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/jira/sprint", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getSprintDetail(@RequestBody MultiValueMap<String, String> formVars) {
        try {
            String userName = utilities.trimString(formVars.get("user_name").toString(), 1);
            String text = utilities.trimString(formVars.get("text").toString(), 1);
            slackAsyncService.logInDB(userName, "Argo Sprint: " + text);
            String sprint = jiraAPI.getArgoSprintDetail(false);
            String sprintDetail = new SlackResponse(sprint).getText();
            SlackResponseAttachment response = slackService.createSlackResponseSprintYesNo(sprintDetail, "sprint");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong, please try again.");
            response.setResponse_type("private");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/jira/sprint/typhoon", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getSprintDetailTyphoon(@RequestBody MultiValueMap<String, String> formVars) {
        try {
            String userName = utilities.trimString(formVars.get("user_name").toString(), 1);
            String text = utilities.trimString(formVars.get("text").toString(), 1);
            slackAsyncService.logInDB(userName, "Typhoon Sprint: " + text);
            String sprint = jiraAPI.getTyphoonSprintDetail(false);
            String sprintDetail = new SlackResponse(sprint).getText();
            SlackResponseAttachment response = slackService.createSlackResponseSprintYesNo(sprintDetail, "typsprint");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong, please try again.");
            response.setResponse_type("private");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/callback", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getBoth(@RequestBody MultiValueMap<String, String> formVars) {

        JSONObject jObject = new JSONObject(formVars.get("payload").get(0));
        String callback_id = jObject.getString("callback_id");
        boolean isYes = jObject.getJSONArray("actions").get(0).toString().contains("yes");
        String responseUrl = jObject.getString("response_url");
        String originalMessage = jObject.getJSONObject("original_message").getString("text");
        String tempWord = originalMessage.split("\n")[0];
        String user = jObject.getJSONObject("user").getString("name");
        String finalString = "";
        try {
            if (callback_id.equalsIgnoreCase("dictionary")) {
                String text = tempWord.substring(17, tempWord.length() - 17);
                if (isYes == true) {
                    String sentence = dictionaryAPI.getSentence(text);
                    finalString = originalMessage + "\n*Sentence:* " + sentence;
                } else {
                    finalString = originalMessage;
                }
                SlackResponse response = new SlackResponse(finalString);
                response.setReplace_original(true);
                return new ResponseEntity<>(response, null, HttpStatus.OK);
            } else if (callback_id.equalsIgnoreCase("sprint")) {
                if (isYes == true) {
                    String updatedSprint = jiraAPI.getArgoSprintDetail(true);
                    finalString = updatedSprint;
                } else {
                    finalString = originalMessage;
                }
                SlackResponse response = new SlackResponse(finalString);
                response.setReplace_original(true);
                return new ResponseEntity<>(response, null, HttpStatus.OK);

            } else if (callback_id.equalsIgnoreCase("typsprint")) {
                if (isYes == true) {
                    String updatedSprint = jiraAPI.getTyphoonSprintDetail(true);
                    finalString = updatedSprint;
                } else {
                    finalString = originalMessage;
                }
                SlackResponse response = new SlackResponse(finalString);
                response.setReplace_original(true);
                return new ResponseEntity<>(response, null, HttpStatus.OK);
            } else if (callback_id.equalsIgnoreCase("triviaFinal")) {
                String reply = "";
                if (isYes == true) {
                    reply = ">*Congratulations <@" + user + ">,  you just mined a new :duck:*";
                    duckService.giveDuck(user);
                } else {
                    reply = ">*Sorry <@" + user + ">, you just lost your duck*";
                    duckService.takeDuck(user);
                }
                SlackResponse response = new SlackResponse(reply);
                response.setReplace_original(true);
                return new ResponseEntity<>(response, null, HttpStatus.OK);
            } else if (callback_id.equalsIgnoreCase("triviaChoice")) {
                int choiceId = Integer.parseInt(jObject.getJSONArray("actions").getJSONObject(0).getString("value"));
                JSONObject jBody = triviaAPI.getTrivia(choiceId);
                SlackResponseAttachment response = slackService.createTrivia(user, jBody, "triviaFinal");
                return new ResponseEntity<>(response, null, HttpStatus.OK);
            } else if (callback_id.equalsIgnoreCase("stealduck")) {
                logger.info("Inside steal duck------------------------------------");
                if (!originalMessage.contains(user)) {
                    if (isYes == true) {
                        String message = originalMessage + "*-@" + user + "-* :white_check_mark:\n";
                        finalString = message;
                    } else {
                        String message = originalMessage + "*-@" + user + "-* :x:\n";
                        finalString = message;
                    }
                    if (utilities.countNumberOfSubstring(finalString, ":x:") >= 3) {
                        SlackResponse response = new SlackResponse("*You did not get enough support for this steal.*");
                        response.setReplace_original(true);
                        return new ResponseEntity<>(response, null, HttpStatus.OK);
                    } else if (utilities.countNumberOfSubstring(finalString, ":white_check_mark:") >= 3) {
                        String receiver = utilities.extractString(originalMessage.substring(0, 20), "(?<=-@)(.*)(?=-)");
                        String giver = utilities.extractString(originalMessage.substring(24), "(?<=-@)(.*)(?=-)");
                        duckService.giveTakeDuck(giver, receiver);
                        SlackResponse response = new SlackResponse("*Congratulations <@" + receiver + ">, you successfully stole a duck from <@" + giver + "> :gun:.*");
                        response.setReplace_original(true);
                        return new ResponseEntity<>(response, null, HttpStatus.OK);
                    }
                } else {
                    finalString = originalMessage;
                }
                SlackResponseAttachment response = slackService.createDuckVote(finalString, "stealduck");
                return new ResponseEntity<>(response, null, HttpStatus.OK);
            } else {
                if (isYes == true) {
                    String updatedSprint = jiraAPI.getArgoSprintDetail(true);
                    finalString = updatedSprint;
                } else {
                    finalString = originalMessage;
                }
                SlackResponse response = new SlackResponse(finalString);
                response.setReplace_original(true);
                return new ResponseEntity<>(response, null, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse(originalMessage + "\nThats all I have.");
            response.setReplace_original(true);
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/duck/give", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> giveDuck(@RequestBody MultiValueMap<String, String> formVars) {
        try {
            String giverUserName = utilities.trimString(formVars.get("user_name").toString(), 1);
            String text = utilities.trimString(formVars.get("text").toString(), 1);
            slackAsyncService.logInDB(giverUserName, "Give duck: " + text);

            String[] splited = text.split("\\s+");
            String receiverUserName = splited[0];
            if (!receiverUserName.contains("@")) {
                SlackResponse response = new SlackResponse("Please try again. Specify username. eg: @kiran");
                response.setResponse_type("private");
                return new ResponseEntity<>(response, null, HttpStatus.OK);
            }
            String replayMessage = duckService.giveDuckCalculation(giverUserName, receiverUserName.substring(1, receiverUserName.length()));
            SlackResponse response = new SlackResponse(replayMessage);
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong, please try again. /props @userName");
            response.setResponse_type("private");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/duck/take", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> takeDuck(@RequestBody MultiValueMap<String, String> formVars) {
        try {
            String receiverUserName = utilities.trimString(formVars.get("user_name").toString(), 1);
            String text = utilities.trimString(formVars.get("text").toString(), 1);
            slackAsyncService.logInDB(receiverUserName, "Stealing duck: " + text);
            String[] splited = text.split("\\s+");
            String giverUserName = splited[0];
            if (!giverUserName.contains("@")) {
                SlackResponse response = new SlackResponse("Please try again. Specify username. eg: @kiran");
                response.setResponse_type("private");
                return new ResponseEntity<>(response, null, HttpStatus.OK);
            }
            giverUserName = giverUserName.substring(1, giverUserName.length());
            if (!duckService.hasEnoughDuck(giverUserName)) {
                SlackResponse response = new SlackResponse("><@" + giverUserName + "> does not have a single duck that you can steal.");
                return new ResponseEntity<>(response, null, HttpStatus.OK);
            }
            SlackResponseAttachment response = slackService.createDuckVote(">*-@" + receiverUserName + "-* wants to steal a duck from *-@" + giverUserName + "-*\n", "stealduck");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong, please try again. /un-props @userName");
            response.setResponse_type("private");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/duck/myduck", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> myDuck(@RequestBody MultiValueMap<String, String> formVars) {
        try {
            String userName = utilities.trimString(formVars.get("user_name").toString(), 1);
            String replayMessage = duckService.getMyDuckDetail(userName);
            SlackResponse response = new SlackResponse(replayMessage);
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong, please try again. /give-duck @userName");
            response.setResponse_type("private");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/duck/duckwinner", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> duckHistory(@RequestBody MultiValueMap<String, String> formVars) {
        try {
            String giverUserName = utilities.trimString(formVars.get("user_name").toString(), 1);
            String replayMessage = duckService.geDuckWinner();
            SlackResponse response = new SlackResponse(replayMessage);
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong, please try again. /give-duck @userName");
            response.setResponse_type("private");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/trivia/question", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> triviaQuestion(@RequestBody MultiValueMap<String, String> formVars) {
        try {
            String user = utilities.trimString(formVars.get("user_name").toString(), 1);
            String text = utilities.trimString(formVars.get("text").toString(), 1);
            slackAsyncService.logInDB(user, "Mining: " + text);
            if (duckService.getDuckCount(user) > 10) {
                SlackResponse response = new SlackResponse("*You already have enough :duck:. Work hard if you want more.*");
                return new ResponseEntity<>(response, null, HttpStatus.OK);
            }
            SlackResponseAttachment response = slackService.createTriviaChoice(user, "triviaChoice");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong, please try again. /give-duck @userName");
            response.setResponse_type("private");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/donate", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> donateNow(@RequestBody MultiValueMap<String, String> formVars) {
        try {
            String user = utilities.trimString(formVars.get("user_name").toString(), 1);
            String text = utilities.trimString(formVars.get("text").toString(), 1);
            slackAsyncService.logInDB(user, "Donating: " + text);
            String donate = jiraAPI.createHyperlink("http://bit.ly/kiranBotPool", "DONATE");
            SlackResponse response = new SlackResponse(">*Thanks for your support.*\n>*" + donate + "*");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong, please try again. /give-duck @userName");
            response.setResponse_type("private");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/support", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> support(@RequestBody MultiValueMap<String, String> formVars) {
        try {
            String user = utilities.trimString(formVars.get("user_name").toString(), 1);
            String text = utilities.trimString(formVars.get("text").toString(), 1);
            slackAsyncService.logInDB(user, "Supporting: " + text);
            String contribute = jiraAPI.createHyperlink("https://github.com/ukiras123/SlackIntegration", "Github Collaborate");
            String donate = jiraAPI.createHyperlink("http://bit.ly/kiranBotPool", "DONATE");
            String participate = jiraAPI.createHyperlink("https://goo.gl/forms/NAPMNsDF6yfZvOsX2", "Participate in Hackathon");
            String feedback = jiraAPI.createHyperlink("https://goo.gl/forms/yDvjgImsU6HdVs9l1", "Feedback");
            SlackResponse response = new SlackResponse(">*How can you help?*\n"
                    + ">*" + donate + "*\n"
                    + ">*" + contribute + "*\n"
                    + ">*" + participate + "*\n"
                    + ">*" + feedback + "*\n");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong, please try again. /give-duck @userName");
            response.setResponse_type("private");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/hackathon", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> hackathon(@RequestBody MultiValueMap<String, String> formVars) {
        try {
            String user = utilities.trimString(formVars.get("user_name").toString(), 1);
            String text = utilities.trimString(formVars.get("text").toString(), 1);
            slackAsyncService.logInDB(user, "Hackathon: " + text);
            String participate = jiraAPI.createHyperlink("https://goo.gl/forms/NAPMNsDF6yfZvOsX2", "Register for Hackathon Now");
            SlackResponse response = new SlackResponse(">*RSVP for 2018 OceanX Hackathon.*\n" +
                    ">*" + "Remember that participation is the first step to win a challenge." + "*\n" +
                    ">*" + participate + "*\n");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SlackResponse response = new SlackResponse("Something went wrong, please try again. /give-duck @userName");
            response.setResponse_type("private");
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }
    }


}



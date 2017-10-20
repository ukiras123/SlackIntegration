package com.kiran.service.schedule;

import com.kiran.service.integration.SlackAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Kiran
 * @since 10/5/17
 */
@Component
public class ScheduledTasks {

    @Autowired
    SlackAPI slackAPI;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "0 0 17 * * *")
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        //slackAPI.sendMessage("Greetings. Please don't forget to log hours. :)", "Reminder");
    }

}

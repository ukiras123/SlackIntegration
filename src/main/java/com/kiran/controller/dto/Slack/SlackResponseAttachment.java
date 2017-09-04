package com.kiran.controller.dto.Slack;


import java.util.List;

/**
 * @author Kiran
 * @since 9/4/17
 */
public class SlackResponseAttachment {
    private String text;

    private String response_type;

    private List<SlackAttachment> attachments;

    public SlackResponseAttachment(String text, List<SlackAttachment> attachments) {
        this.text = text;
        this.response_type = "in_channel";
        this.attachments = attachments;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getResponse_type() {
        return response_type;
    }

    public void setResponse_type(String response_type) {
        this.response_type = response_type;
    }

    public List<SlackAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<SlackAttachment> attachments) {
        this.attachments = attachments;
    }


}

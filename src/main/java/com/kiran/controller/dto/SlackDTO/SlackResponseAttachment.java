package com.kiran.controller.dto.SlackDTO;


import java.util.List;

/**
 * @author Kiran
 * @since 9/4/17
 */
public class SlackResponseAttachment<T> {
    private String text;

    private String response_type;

    private List<T> attachments;


    public SlackResponseAttachment(String text, List<T> attachments) {
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

    public List<T> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<T> attachments) {
        this.attachments = attachments;
    }


}

package com.kiran.controller.dto.SlackDTO;

import java.util.List;

/**
 * @author Kiran
 * @since 11/16/17
 */

public class SlackInteractiveAttachment<T> {

    private String text;
    private String fallback;
    private String callback_id;
    private String color;
    private String attachment_type;

    private List<T> actions;

    public SlackInteractiveAttachment(String text, String fallback, String callback_id, List<T> actions) {
        this.text = text;
        this.fallback = fallback;
        this.callback_id = callback_id;
        this.color = "#3AA3E3";
        this.attachment_type = "default";
        this.actions = actions;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFallback() {
        return fallback;
    }

    public void setFallback(String fallback) {
        this.fallback = fallback;
    }

    public String getCallback_id() {
        return callback_id;
    }

    public void setCallback_id(String callback_id) {
        this.callback_id = callback_id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAttachment_type() {
        return attachment_type;
    }

    public void setAttachment_type(String attachment_type) {
        this.attachment_type = attachment_type;
    }

    public List<T> getActions() {
        return actions;
    }

    public void setActions(List<T> actions) {
        this.actions = actions;
    }
}

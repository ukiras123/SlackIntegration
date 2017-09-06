package com.kiran.controller.dto.SlackDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Kiran
 * @since 9/4/17
 */
public class SlackAttachmentFields {
    private String title;
    private String value;

    @JsonProperty(value = "short")
    private boolean isShort;


    public SlackAttachmentFields(String title, String value, boolean isShort) {
        this.title = title;
        this.value = value;
        this.isShort = isShort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

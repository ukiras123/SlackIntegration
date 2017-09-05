package com.kiran.controller.dto.Slack;

import java.util.List;

/**
 * @author Kiran
 * @since 9/4/17
 */
public class SlackAttachment {

    private String title;
    private String title_link;
    private String text;
    private String image_url;

    private List<SlackAttachmentFields> fields;


    public SlackAttachment(String title, String title_link, String text, String image_url, List<SlackAttachmentFields> fields) {
        this.title = title;
        this.title_link = title_link;
        this.text = text;
        this.image_url = image_url;
        this.fields = fields;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_link() {
        return title_link;
    }

    public void setTitle_link(String title_link) {
        this.title_link = title_link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<SlackAttachmentFields> getFields() {
        return fields;
    }

    public void setFields(List<SlackAttachmentFields> fields) {
        this.fields = fields;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}

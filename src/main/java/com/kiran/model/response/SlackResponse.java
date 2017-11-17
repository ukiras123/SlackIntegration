package com.kiran.model.response;

/**
 * @author Kiran
 * @since 8/29/17
 */
public class SlackResponse {

    private String text;

    private String response_type;


    private boolean replace_original;

    public SlackResponse(String text) {
        this.text = text;
        this.response_type = "in_channel";
    }

    public SlackResponse(String text, boolean isPrivate) {
        this.text = text;
        if (isPrivate == true) {
            this.response_type = "ephemeral";
        } else {
            this.response_type = "in_channel";
        }
    }

    public boolean isReplace_original() {
        return replace_original;
    }

    public void setReplace_original(boolean replace_original) {
        this.replace_original = replace_original;
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
}

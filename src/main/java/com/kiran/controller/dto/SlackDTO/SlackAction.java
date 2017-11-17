package com.kiran.controller.dto.SlackDTO;

/**
 * @author Kiran
 * @since 11/16/17
 */
public class SlackAction {
    private String name;
    private String text;
    private String type;
    private String style;
    private String value;

    public SlackAction(String text, String type, String style, String value) {
        this.name = "option";
        this.text = text;
        this.type = type;
        this.style = style;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

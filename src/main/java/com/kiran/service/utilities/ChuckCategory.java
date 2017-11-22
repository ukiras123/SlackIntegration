package com.kiran.service.utilities;

/**
 * @author Kiran
 * @since 11/19/17
 */
public enum ChuckCategory {
    DEV("dev"),
    MOVIE("movie"),
    FOOD("food"),
    CELEBRITY("celebrity"),
    SCIENCE("science"),
    POLITICAL("political"),
    SPORT("sport"),
    RELIGION("religion"),
    ANIMAL("animal"),
    MUSIC("music"),
    HISTORY("history"),
    TRAVEL("travel"),
    CAREER("career"),
    MONEY("money"),
    FASHION("fashion");

    private String name;

    ChuckCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

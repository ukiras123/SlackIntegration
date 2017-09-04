package com.kiran.service.utilities;

import org.springframework.stereotype.Component;

/**
 * @author Kiran
 * @since 8/29/17
 */

@Component
public class Utilities {

    public Utilities() {
    }

    public String trimString(String str, int trim_size)
    {
       return str.substring(trim_size, str.length()-trim_size);
    }

    public enum WIT_ENTITIES {
        FOOD("food"),
        WEATHER("weather"),
        RESTAURNT("restaurant"),
        LOCATION("location"),
        DATETIME("datetime"),
        INTENT("intent");

        private String name;
        WIT_ENTITIES(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

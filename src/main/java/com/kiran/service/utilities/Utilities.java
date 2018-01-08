package com.kiran.service.utilities;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public int countNumberOfSubstring(String originalString, String subString) {
        String str = originalString;
        String findStr = subString;
        int lastIndex = 0;
        int count = 0;

        while(lastIndex != -1){

            lastIndex = str.indexOf(findStr,lastIndex);

            if(lastIndex != -1){
                count ++;
                lastIndex += findStr.length();
            }
        }
        return count;
    }

    public String extractString(String text, String regex)  {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        String string = "";
        while (m.find()) {
            string = m.group();
        }
        return string;
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

package com.kiran.service.utilities;

import org.springframework.stereotype.Component;

import java.util.Random;
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

    public String trimString(String str, int trim_size) {
        return str.substring(trim_size, str.length() - trim_size);
    }

    public int countNumberOfSubstring(String originalString, String subString) {
        String str = originalString;
        String findStr = subString;
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {

            lastIndex = str.indexOf(findStr, lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        return count;
    }

    public String extractString(String text, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        String string = "";
        while (m.find()) {
            string = m.group();
        }
        return string;
    }


    public void shuffleArray(String[] a) {
        int n = a.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(a, i, change);
        }
    }

    private static void swap(String[] a, int i, int change) {
        String helper = a[i];
        a[i] = a[change];
        a[change] = helper;
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


    public enum TRIVIA_CATEGORIES {
        GeneralKnowledge("GeneralKnowledge", 9),
        ScienceComputer("ScienceComputer", 18),
        Sports("Sports", 21),
        History("History", 23),
        Vehicles("Vehicles", 28),
        ScienceGadgets("ScienceGadgets", 30);
        private String name;
        private int id;

        TRIVIA_CATEGORIES(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}

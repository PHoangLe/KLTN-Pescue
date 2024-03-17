package com.project.pescueshop.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Util {

    public static List<String> getListStringFromString(String string){
        String[] stringArray = string.split("\\^\\|");
        return Arrays.asList(stringArray);
    }

    public static Date getCurrentDate(){
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date getCurrentDatePlusSeconds(long seconds){
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        zonedDateTime = zonedDateTime.plusSeconds(seconds);
        return Date.from(zonedDateTime.toInstant());
    }

    public static String getRandomKey() {
        String dictionary = "aAbB9cCdDeE0fFgGhHiI1jJkKlLmM2nNoOpP3qQrRsStT4uU5vV6wWy7zZ8";
        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++){
            builder.append(dictionary.charAt(random.nextInt(0, dictionary.length())));
        }

        return builder.toString();
    }

    public static Date getCurrentDateMinusDays(long day) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        zonedDateTime = zonedDateTime.minusDays(day);
        return Date.from(zonedDateTime.toInstant());
    }
}

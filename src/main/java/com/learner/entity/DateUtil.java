package com.learner.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static String getCurrentDateInYYYYMMDDHHMMSS() {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        
        // Define the desired date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        // Format the date and time to the desired format
        return currentDateTime.format(formatter);
    }
}

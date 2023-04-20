package com.example.uhf.tools;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Locale;

public  class DateHelper {
    public static class DateHelperClassStaticHelper {
        public static String getCurrentDateWithT() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = new Date();
            String specific = formatter.format(date);
            return specific;
        }

        public static String getCurrentDateWithoutT() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String specific = formatter.format(date);
            return specific;
        }
    }
}

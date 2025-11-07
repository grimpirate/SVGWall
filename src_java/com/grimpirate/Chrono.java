package com.grimpirate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Chrono
{
    // https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
    public String ofPattern(String pattern)
    {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public String iso8601()
    {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
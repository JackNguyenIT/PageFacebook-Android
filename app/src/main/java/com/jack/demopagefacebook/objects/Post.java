package com.jack.demopagefacebook.objects;

import android.text.format.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Created by Jack on 3/14/17.
 */

public class Post {
    public String id;
    public String type;
    public String picture;
    public String full_picture;
    public String description;
    public String message;
    public String caption;
    public String name;
    public String created_time;
    public String link;

    public CharSequence getTimeCreated() {
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").parseDateTime(created_time);
        DateTimeZone timeZone = DateTimeZone.getDefault();

        CharSequence d;
        LocalDate date = new LocalDate(dateTime, timeZone);
        LocalDate now = LocalDate.now();
        if (now.plusDays(-1).equals(date) || now.equals(date)) {
            d = DateUtils.getRelativeTimeSpanString(date.toDate().getTime(), now.toDate().getTime(), DateUtils.DAY_IN_MILLIS);
        } else {
            d = DateTimeFormat.forPattern("dd MMM yyyy").print(date);
        }

        String time = DateTimeFormat.shortTime().withZone(timeZone).print(dateTime);
        return String.format("%s %s", d, time);
    }

}

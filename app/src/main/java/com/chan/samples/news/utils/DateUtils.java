package com.chan.samples.news.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by chan on 1/14/18.
 */

public class DateUtils {

    public static final Map<String, Long> times = new LinkedHashMap<>();
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'hh:mm:ss'Z'";

    private static final SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_PATTERN);

    static {
        times.put("year", TimeUnit.DAYS.toMillis(365));
        times.put("month", TimeUnit.DAYS.toMillis(30));
        times.put("week", TimeUnit.DAYS.toMillis(7));
        times.put("day", TimeUnit.DAYS.toMillis(1));
        times.put("hour", TimeUnit.HOURS.toMillis(1));
        times.put("minute", TimeUnit.MINUTES.toMillis(1));
        times.put("second", TimeUnit.SECONDS.toMillis(1));
    }

    public static String toRelative(long duration, int maxLevel) {
        StringBuilder res = new StringBuilder();
        int level = 0;
        for (Map.Entry<String, Long> time : times.entrySet()){
            long timeDelta = duration / time.getValue();
            if (timeDelta > 0){
                res.append(timeDelta)
                        .append(" ")
                        .append(time.getKey())
                        .append(timeDelta > 1 ? "s" : "")
                        .append(", ");
                duration -= time.getValue() * timeDelta;
                level++;
            }
            if (level == maxLevel){
                break;
            }
        }
        if ("".equals(res.toString())) {
            return "0 seconds ago";
        } else {
            res.setLength(res.length() - 2);
            res.append(" ago");
            return res.toString();
        }
    }

    public static String toRelative(long duration) {
        return toRelative(duration, times.size());
    }

    public static String toRelative(Date start, Date end){
        return toRelative(end.getTime() - start.getTime());
    }

    public static String toRelative(Date start, Date end, int level){
        return toRelative(end.getTime() - start.getTime(), level);
    }


    public static String format(String date){
        return toRelative(getDate(date),new Date(),1);
    }

    public static Date getDate(String date){
        try {
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("TAG",e.getMessage());
        }
        Log.i("TAG","parse failed");
        return new Date();
    }
}

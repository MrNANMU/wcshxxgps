package gpsutils.wcshxx.com.gps.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String formatTo(String timeStr,String pattern){
        return "";
    }

    public static String longToString(long time,String pattern){
        if (time == 0){
            return "";
        }
        Date date = new Date(time);
        SimpleDateFormat sdf = null;
        try {
            sdf = new SimpleDateFormat(pattern);
        } catch (Exception e){
            e.printStackTrace();
        }
        String strs = sdf.format(date);
        return strs;
    }
}

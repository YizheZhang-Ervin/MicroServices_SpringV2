package pers.ervin.redisdemo.utils;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date addMinutes(Date date, int minutes){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return new Time (cal.getTime().getTime());
    }
}

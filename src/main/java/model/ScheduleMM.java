package model;

import constants.FormatConstants;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

@Data
public class ScheduleMM {
    private Long showDateTime;
    private String showTime;
    private String showTimeDuration;
    private String screenNumber;
    private String screenName;
    private String sessionTime;
    private String sessionEndTime;

    public static ScheduleMM fromMap(LinkedHashMap<String, Object> map) {
        ScheduleMM scheduleMM = new ScheduleMM();
        scheduleMM.setScreenName(getOrDefault("ScreenName", map));
        scheduleMM.setScreenNumber(getOrDefault("ScreenNumber", map));
        scheduleMM.setShowDateTime(getOrDefault("ShowDateTime", map));
        scheduleMM.setShowTime(getOrDefault("ShowTime", map));
        scheduleMM.setShowTimeDuration(getOrDefault("ShowTimeDuration", map));
        return scheduleMM;
    }

    public static ScheduleMM fromMapZL(LinkedHashMap<String, Object> map) {
        ScheduleMM scheduleMM = new ScheduleMM();
        scheduleMM.setSessionTime(getOrDefault("sessionTime", map));
        scheduleMM.setSessionEndTime(getOrDefault("sessionEndTime", map));
        // screen duration
        try {
            Date startTime = new SimpleDateFormat(FormatConstants.FORMAT_DATE_STRING).parse(scheduleMM.getSessionTime());
            Date endTime = new SimpleDateFormat(FormatConstants.FORMAT_DATE_STRING).parse(scheduleMM.getSessionEndTime());
            scheduleMM.setShowTimeDuration(getScreenDuration(startTime, endTime));
        } catch (Exception e){
            scheduleMM.setShowTimeDuration("");
        }
        return scheduleMM;
    }

    private static <T> T getOrDefault(String key, LinkedHashMap<String, Object> map){
        return (T) map.getOrDefault(key, null);
    }

    private static String getScreenDuration(Date start, Date end) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(start) + " ~ " + format.format(end);
    }
}

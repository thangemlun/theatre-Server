package model;

import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class ScheduleMM {
    private Long showDateTime;
    private String showTime;
    private String showTimeDuration;
    private String screenNumber;
    private String screenName;

    public static ScheduleMM fromMap(LinkedHashMap<String, Object> map) {
        ScheduleMM scheduleMM = new ScheduleMM();
        scheduleMM.setScreenName(getOrDefault("ScreenName", map));
        scheduleMM.setScreenNumber(getOrDefault("ScreenNumber", map));
        scheduleMM.setShowDateTime(getOrDefault("ShowDateTime", map));
        scheduleMM.setShowTime(getOrDefault("ShowTime", map));
        scheduleMM.setShowTimeDuration(getOrDefault("ShowTimeDuration", map));
        return scheduleMM;
    }

    private static <T> T getOrDefault(String key, LinkedHashMap<String, Object> map){
        return (T) map.getOrDefault(key, null);
    }
}

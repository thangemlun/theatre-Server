package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CinemaMM {
    private String cityName;
    private String name;
    private String address;
    private String avatar;
    private String logo;
    private Integer ratingCount;
    private Double ratingValue;
    private List<ScheduleMM> schedules;

    public static CinemaMM fromMap(LinkedHashMap<String, Object> map) {
        CinemaMM item = new CinemaMM();
        item.setCityName(getOrDefault("CityName", map));
        item.setName(getOrDefault("Name", map));
        item.setLogo(getOrDefault("Logo", map));
        item.setAvatar(getOrDefault("Avatar", map));
        item.setAddress(getOrDefault("Address", map));
        item.setRatingCount(getOrDefault("RatingCount", map));
        item.setRatingValue(getOrDefault("RatingValue", map));
        ArrayList<LinkedHashMap<String, Object>> scheduleMap = (ArrayList<LinkedHashMap<String, Object>>) map.get("VersionsCaptions");
        List<ScheduleMM> schedules = new ArrayList<>();
        for (LinkedHashMap<String, Object> mapItem : scheduleMap) {
            ArrayList<LinkedHashMap<String, Object>> scheduleDataMap = (ArrayList<LinkedHashMap<String, Object>>) mapItem.get("ShowTimes");
            schedules.addAll(scheduleDataMap.stream().map(ScheduleMM::fromMap).collect(Collectors.toList()));
        }
        item.setSchedules(schedules);
        return item;
    }



    private static <T> T getOrDefault(String key, LinkedHashMap<String, Object> map){
        return (T) map.getOrDefault(key, null);
    }
}

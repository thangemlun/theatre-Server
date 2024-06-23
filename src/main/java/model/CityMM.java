package model;

import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class CityMM {
    private Integer apiId;
    private String name;
    private String code;

    public static CityMM fromMap(LinkedHashMap<String, Object> map) {
        CityMM result = new CityMM();
        result.setApiId(getOrDefault("ApiId", map));
        result.setCode(getOrDefault("Code", map));
        result.setName(getOrDefault("Name", map));
        return result;
    }

    private static <T> T getOrDefault(String key,LinkedHashMap<String, Object> map){
        return (T) map.getOrDefault(key, null);
    }
}

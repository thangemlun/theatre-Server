package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class CityMM {
    private Integer apiId;
    private String name;
    private String code;

    public CityMM(int apiId, String name, String code) {
        this.apiId = apiId;
        this.name = name;
        this.code = code;
    }

    public static List<CityMM> initCities() {
        List<CityMM> results = new ArrayList<>();
        results.add(new CityMM(1, "TP.Hồ Chí Minh", "HCM"));
        results.add(new CityMM(2, "TP.Hà Nội", "HaNoi"));
        results.add(new CityMM(3, "TP.Đà Nẵng", "DaNang"));
        results.add(new CityMM(6, "TP.Nha Trang", "NhaTrang"));
        results.add(new CityMM(10, "TP.Cần Thơ", "CanTho"));
        results.add(new CityMM(14, "TP.Long Xuyên", "LongXuyen"));
        results.add(new CityMM(46, "TP.Sóc Trăng", "SocTrang"));

        return results;
    }

    private static <T> T getOrDefault(String key,LinkedHashMap<String, Object> map){
        return (T) map.getOrDefault(key, null);
    }
}

package model;

import lombok.*;
import utils.Json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieMM {
    private Integer Id;
    private String Title;
    private String ApiFilmId;
    private String BannerUrl;
    private Integer Duration;
    private String GraphicUrl;
    private String ApiFilmType;
    private String ApiRating;
    private String ApiCaptionType;
    private String ApiRatingFormat;

    private String ApiGenreName;
    private String OpeningDate;

    private String Synopsis;

    private String SynopsisEn;

    private String TitleEn;

    private String TrailerUrl;

    private Double imdbPoint;

    private List<String> images;

    public static MovieMM fromMap(LinkedHashMap<String, Object> map) {
        MovieMM movie = new MovieMM();
        movie.setId(getOrDefault("Id", map));
        movie.setApiFilmId(getOrDefault("ApiFilmId", map));
        movie.setApiCaptionType(getOrDefault("ApiCaptionType", map));
        movie.setApiRating(getOrDefault("ApiRating", map));
        movie.setDuration(getOrDefault("Duration", map));
        movie.setSynopsis(getOrDefault("Synopsis", map));
        movie.setSynopsisEn(getOrDefault("SynopsisEn", map));
        movie.setTitle(getOrDefault("Title", map));
        movie.setTitleEn(getOrDefault("TitleEn", map));
        movie.setApiGenreName(getOrDefault("ApiGenreName", map));
        movie.setOpeningDate(getOrDefault("OpeningDate", map));
        movie.setBannerUrl(getOrDefault("BannerUrl", map));
        movie.setGraphicUrl(getOrDefault("GraphicUrl", map));
        movie.setTrailerUrl(getOrDefault("TrailerUrl", map));
        movie.setApiRatingFormat(getOrDefault("ApiRatingFormat", map));
        movie.setApiFilmType(getOrDefault("ApiFilmType", map));
        return movie;
    }

    public static MovieMM fromMapZL(LinkedHashMap<String, Object> map) {
        MovieMM movie = new MovieMM();
        movie.setId(getOrDefault("id", map));
        movie.setApiFilmId(""+getOrDefault("id", map));
        movie.setApiCaptionType(getOrDefault("filmVersion", map));
        movie.setApiRating(getRating(getOrDefault("age", map)));
        movie.setDuration(getOrDefault("duration", map));
        movie.setSynopsis(getOrDefault("desc", map));
        movie.setSynopsisEn(getOrDefault("desc", map));
        movie.setTitle(getOrDefault("name", map));
        movie.setTitleEn(getOrDefault("nameEN", map));
        movie.setApiGenreName(getOrDefault("category", map));
        movie.setOpeningDate(getOrDefault("publishDate", map));
        ZaloImage images = Json.defaultObjectMapper().convertValue(getOrDefault("images", map), ZaloImage.class);
        movie.setBannerUrl(alternativeValue(images.getBanner(), images.getType1_path()));
        movie.setGraphicUrl(images.getType1_size1());
        movie.setTrailerUrl(getOrDefault("url", map));
        movie.setApiFilmType(getOrDefault("filmVersion", map));
        movie.setImdbPoint(getNumber(getOrDefault("imdbPoint", map)));
        return movie;
    }

    private static Double getNumber(Object imdbPoint) {
        Double result = 0.0;
        if (imdbPoint != null) {
            if (imdbPoint instanceof Integer) {
                result = Integer.valueOf((int) imdbPoint).doubleValue();
            }
            if (imdbPoint instanceof Double) {
                result = (Double) imdbPoint;
            }
        }

        return result;
    }

    private static <T> T getOrDefault(String key,LinkedHashMap<String, Object> map){
        return (T) map.getOrDefault(key, null);
    }

    private static String getRating(int age) {
        if (age < 13) {
            return "P";
        } else {
            return age+"+";
        }
    }

    private static <T> T alternativeValue(Object value1, Object value2) {
        return (T) (value1 != null ? value1 : value2 != null ?  value2 : null);
    }

}

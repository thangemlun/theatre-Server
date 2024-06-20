package model;

import lombok.*;

import java.util.LinkedHashMap;

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

    private static <T> T getOrDefault(String key,LinkedHashMap<String, Object> map){
        return (T) map.getOrDefault(key, null);
    }

}

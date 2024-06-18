package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private Long id;
    private String name;
    private String slug;
    private String en_name;
    private String poster;
    private Long star_rating_value;
    private Long star_rating_count;
    private String seo_description;
    private String duration;
    private String imdb;
    private String release;
    private String year;
    private Date releaseDate;
    private String trailer;
}


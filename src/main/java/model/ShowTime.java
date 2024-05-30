package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowTime {
    private int id;
    private String date;
    private Map<String, List<ScreenTime>> time;
    private Long movie_id;
    private long cinema_id;
    private String name;
    private String poster;
    private String duration;
    private String slug;
}

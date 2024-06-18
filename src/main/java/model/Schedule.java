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
public class Schedule {
    private String title;
    private Date start_time;
    private String channel_id;
    private String full_title;
    private Date end_time;
    private String _id;
}

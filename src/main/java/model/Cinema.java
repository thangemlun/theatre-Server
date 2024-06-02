package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cinema {
    private long id;
    private String name;
    private String slug;
    private String city;
    private String image;
    private String address;
}

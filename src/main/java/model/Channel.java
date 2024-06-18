package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    private String _id;
    private String original_logo;
    private String alias_name;
    private String name;
    private String thumb;
    private String website_url;
}

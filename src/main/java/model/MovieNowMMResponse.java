package model;

import lombok.Data;

@Data
public class MovieNowMMResponse {
    Boolean Result;
    CommonResponse<MovieMM> Data;
}

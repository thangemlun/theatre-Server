package model;

import lombok.Data;

@Data
public class MovieSoonMMResponse {
    Boolean Result;
    CommonResponse<MovieMM> Data;
}

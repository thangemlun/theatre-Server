package model;

import lombok.Data;

import java.util.List;

@Data
public class CinemaMasterResponse {
    private Boolean result;
    private List<CityMM> cities;
}

package model;

import lombok.Data;

import java.util.List;

@Data
public class CommonResponse<T>{
    private Integer Count;
    private Integer LastIndex;
    private Integer PageCount;
    private Integer TotalItems;
    List<T> Items;
}

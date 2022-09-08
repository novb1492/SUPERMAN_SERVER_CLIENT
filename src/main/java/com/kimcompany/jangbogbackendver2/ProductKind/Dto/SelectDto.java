package com.kimcompany.jangbogbackendver2.ProductKind.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SelectDto {

    private String name;
    private long id;

    public SelectDto(String name, long id) {
        this.name = name;
        this.id = id;
    }
}

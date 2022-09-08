package com.kimcompany.jangbogbackendver2.Store.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SelectNotyDto {
    private String name;
    private String addr;

    public SelectNotyDto(String name, String addr) {
        this.name = name;
        this.addr = addr;
    }
}

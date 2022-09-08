package com.kimcompany.jangbogbackendver2.Company.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SelectListDto {

    private long id;
    private String companyNum;

    public SelectListDto(long id, String companyNum) {
        this.id = id;
        this.companyNum = companyNum;
    }
}

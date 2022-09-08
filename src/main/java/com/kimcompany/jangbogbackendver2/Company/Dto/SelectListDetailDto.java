package com.kimcompany.jangbogbackendver2.Company.Dto;

import com.kimcompany.jangbogbackendver2.Company.Model.CompanyEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SelectListDetailDto {

    private String name;
    private String openDate;
    private long id;
    private String num;


    @QueryProjection
    public SelectListDetailDto(CompanyEntity companyEntity) {
        this.name = companyEntity.getName();
        this.openDate = companyEntity.getOpenDate();
        this.id = companyEntity.getId();
        this.num= companyEntity.getCompanyNum();
    }
}

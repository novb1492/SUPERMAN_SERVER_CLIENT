package com.kimcompany.jangbogbackendver2.Employee.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class NotyEmployeeDto {

    private String phone;
    private String email;

    @QueryProjection
    public NotyEmployeeDto(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }
}

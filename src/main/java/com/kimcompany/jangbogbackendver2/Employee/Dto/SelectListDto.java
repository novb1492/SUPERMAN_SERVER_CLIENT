package com.kimcompany.jangbogbackendver2.Employee.Dto;

import com.kimcompany.jangbogbackendver2.Employee.Model.EmployeeEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SelectListDto {
    private String name;
    private String phone;
    private String email;
    private String role;
    private long employeeId;

    @QueryProjection
    public SelectListDto(MemberEntity memberEntity, EmployeeEntity employeeEntity) {
        this.name = memberEntity.getFirstName()+memberEntity.getLastName();
        this.phone = memberEntity.getPhone();
        this.email = memberEntity.getEmail();
        this.role = memberEntity.getRole();
        this.employeeId = employeeEntity.getId();
    }


}

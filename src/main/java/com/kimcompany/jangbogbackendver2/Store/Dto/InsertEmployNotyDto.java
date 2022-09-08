package com.kimcompany.jangbogbackendver2.Store.Dto;

import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class InsertEmployNotyDto {
    private String adminPhone;
    private String employeePhone;
    private String adminEmail;
    private String employeeEmail;
    private String storeName;
    private String address;
    private String storeTel;
    private String employeeUserId;
    private String employeeName;

    @QueryProjection
    public InsertEmployNotyDto(MemberEntity memberEntity, StoreEntity storeEntity){
        this.address=storeEntity.getAddressColumn().getAddress();
        this.employeePhone=memberEntity.getPhone();
        this.employeeEmail=memberEntity.getEmail();
        this.storeName=storeEntity.getName();
        this.storeTel=storeEntity.getTel();
        this.employeeUserId=memberEntity.getUserId();
        this.employeeName=memberEntity.getFirstName()+memberEntity.getLastName();
    }

}

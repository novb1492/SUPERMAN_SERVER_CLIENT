package com.kimcompany.jangbogbackendver2.Store.Dto;

import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class SelectRegiDto {

    private String name;
    private String address;

    private Long id;

    @QueryProjection
    public SelectRegiDto(StoreEntity storeEntity){
        this.name = storeEntity.getName();
        this.address = storeEntity.getAddressColumn().getAddress();
        this.id = storeEntity.getId();
    }
}

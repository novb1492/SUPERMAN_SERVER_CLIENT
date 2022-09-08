package com.kimcompany.jangbogbackendver2.Store.Dto;

import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SelectListDto {
    private long id;
    private String name;
    private int state;
    private String imgPath;
    private String address;

    @QueryProjection
    public SelectListDto(StoreEntity storeEntity) {
        id=storeEntity.getId();
        name=storeEntity.getName();
        imgPath=storeEntity.getThumbNail();
        address=storeEntity.getAddressColumn().getAddress();
        state=storeEntity.getCommonColumn().getState();
    }
}

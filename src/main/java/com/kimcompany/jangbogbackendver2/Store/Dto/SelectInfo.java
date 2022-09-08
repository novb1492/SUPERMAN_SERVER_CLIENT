package com.kimcompany.jangbogbackendver2.Store.Dto;

import com.kimcompany.jangbogbackendver2.Common.AddressColumn;
import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Company.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Data
public class SelectInfo {

    private Long id;
    private Integer minOrderPrice;
    private Double maxDeliverRadius;
    private String openTime;
    private String closeTime;
    private String tel;
    private String text;
    private String name;
    private String thumbNail;
    private String insertDate;
    private Integer state;
    private String address;
    private String postcode;
    private String detailAddress;
    private long companyNum;
    private List<SelectListDto> companyNums;

    @QueryProjection
    public SelectInfo(StoreEntity storeEntity) {
        id=storeEntity.getId();
        minOrderPrice=storeEntity.getMinOrderPrice();
        maxDeliverRadius=storeEntity.getMaxDeliverRadius();
        openTime=storeEntity.getOpenTime();
        closeTime=storeEntity.getCloseTime();
        tel=storeEntity.getTel();
        text=storeEntity.getText();
        name=storeEntity.getName();
        thumbNail=storeEntity.getThumbNail();
        state=storeEntity.getCommonColumn().getState();
        address=storeEntity.getAddressColumn().getAddress();
        postcode=storeEntity.getAddressColumn().getPostCode();
        detailAddress=storeEntity.getAddressColumn().getDetailAddress();
        companyNum=storeEntity.getCompanyEntity().getId();
    }
}

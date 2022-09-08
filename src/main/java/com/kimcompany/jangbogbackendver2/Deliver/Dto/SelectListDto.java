package com.kimcompany.jangbogbackendver2.Deliver.Dto;

import com.kimcompany.jangbogbackendver2.Common.AddressColumn;
import com.kimcompany.jangbogbackendver2.Deliver.Model.DeliverEntity;
import com.kimcompany.jangbogbackendver2.Order.Model.OrderEntity;
import com.kimcompany.jangbogbackendver2.Payment.Model.CardEntity;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SelectListDto {

    private String address;
    private String detailAddress;
    private String postCode;
    private long cardId;
    private long deliverRoomId;
    private int price;

    @QueryProjection
    public SelectListDto(CardEntity cardEntity, OrderEntity orderEntity, DeliverEntity deliverEntity) {
        AddressColumn addressColumn = orderEntity.getAddressColumn();
        this.address = addressColumn.getAddress();
        this.detailAddress = addressColumn.getDetailAddress();
        this.postCode = addressColumn.getPostCode();
        this.cardId = cardEntity.getId();
        this.deliverRoomId = deliverEntity.getId();
        this.price = cardEntity.getCommonPaymentEntity().getPrtcRemains();
    }



}

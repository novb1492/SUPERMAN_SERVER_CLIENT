package com.kimcompany.jangbogbackendver2.Deliver.Dto;

import com.kimcompany.jangbogbackendver2.Common.AddressColumn;
import com.kimcompany.jangbogbackendver2.Deliver.Model.DeliverDetailEntity;
import com.kimcompany.jangbogbackendver2.Deliver.Model.DeliverEntity;
import com.kimcompany.jangbogbackendver2.Order.Model.OrderEntity;
import com.kimcompany.jangbogbackendver2.Payment.Model.CardEntity;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SelectDto {

    private long orderId;
    private long cardId;
    private long deliverId;
    private long deliverDetailId;
    private String address;
    private String postcode;
    private String detailAddress;
    private String price;
    private int state;

    @QueryProjection
    public SelectDto(CardEntity cardEntity, OrderEntity orderEntity, DeliverDetailEntity deliverDetailEntity) {
        AddressColumn addressColumn = orderEntity.getAddressColumn();
        this.orderId = orderEntity.getId();
        this.cardId = cardEntity.getId();
        this.address = addressColumn.getAddress();
        this.postcode = addressColumn.getPostCode();
        this.detailAddress = addressColumn.getDetailAddress();
        this.price = UtilService.confirmPrice(cardEntity.getCommonPaymentEntity().getPrtcRemains());
        this.deliverDetailId = deliverDetailEntity.getId();
        this.deliverId = deliverDetailEntity.getDeliverEntity().getId();
        this.state = deliverDetailEntity.getCommonColumn().getState();
    }
}

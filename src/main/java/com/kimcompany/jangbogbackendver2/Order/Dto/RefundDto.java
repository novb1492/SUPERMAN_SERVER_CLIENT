package com.kimcompany.jangbogbackendver2.Order.Dto;

import com.kimcompany.jangbogbackendver2.Order.Model.OrderEntity;
import com.kimcompany.jangbogbackendver2.Payment.Model.CardEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RefundDto {
    private OrderEntity orderEntity;
    private CardEntity cardEntity;

    @QueryProjection
    public RefundDto(OrderEntity orderEntity, CardEntity cardEntity) {
        this.orderEntity = orderEntity;
        this.cardEntity = cardEntity;
    }
}

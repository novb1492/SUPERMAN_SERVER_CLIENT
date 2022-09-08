package com.kimcompany.jangbogbackendver2.Payment.Dto;

import com.kimcompany.jangbogbackendver2.Payment.Model.CardEntity;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SelectForOrderDto {
    private String cardNum;
    private String bankName;
    private String totalPrice;

    public SelectForOrderDto(CardEntity cardEntity) {
        this.cardNum = cardEntity.getP_CARD_NUM();
        this.bankName = cardEntity.getP_FN_NM();
        this.totalPrice = UtilService.confirmPrice( cardEntity.getCommonPaymentEntity().getPrtcRemains());
    }
}

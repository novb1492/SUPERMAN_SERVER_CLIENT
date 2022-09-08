package com.kimcompany.jangbogbackendver2.Order.Dto;

import com.kimcompany.jangbogbackendver2.Member.Model.ClientEntity;
import com.kimcompany.jangbogbackendver2.Order.Model.OrderEntity;
import com.kimcompany.jangbogbackendver2.Payment.Dto.SelectForOrderDto;
import com.kimcompany.jangbogbackendver2.Payment.Model.CardEntity;
import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import com.kimcompany.jangbogbackendver2.ProductEvent.Model.ProductEventEntity;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class SelectDto {
    /**
     * 주문정보
     */
    private Long id;
    private Integer price;
    private Integer totalCount;
    private Integer state;
    /**
     * 제품정보
     */
    private String productName;
    private String productImgPath;
    /**
     * 제품이벤트 정보
     */
    private String eventName;
    @QueryProjection
    public SelectDto(OrderEntity orderEntity, ProductEntity productEntity,ProductEventEntity productEventEntity) {
        id = orderEntity.getId();
        price = orderEntity.getPrice();
        totalCount = orderEntity.getTotalCount();
        productImgPath = productEntity.getProductImgPath();
        productName = productEntity.getName();
        state = orderEntity.getCommonColumn().getState();
        if(orderEntity.getProductEventEntity()!=null){
            eventName = productEventEntity.getName();
        }else{
            eventName="이벤트 없이 구매";
        }
    }

}

package com.kimcompany.jangbogbackendver2.Cart.Dto;

import com.kimcompany.jangbogbackendver2.Cart.Model.CartEntity;
import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import com.kimcompany.jangbogbackendver2.ProductEvent.Model.ProductEventEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SelectListDto {

    private long id;
    private boolean eventFlag;
    private int count;
    private long productId;
    private String productName;
    private long  eventId;
    private String eventName;


    @QueryProjection
    public SelectListDto(CartEntity cartEntity, ProductEntity productEntity, ProductEventEntity productEventEntity) {
        this.id = cartEntity.getId();
        this.count = cartEntity.getCount();
        this.productId = productEntity.getId();
        this.productName = productEntity.getName();
        if(productEventEntity!=null){
            eventFlag=true;
            eventId=productEventEntity.getId();
            eventName=productEventEntity.getName();
        }else{
            eventFlag=false;
            eventId=0;
            eventName=null;
        }
    }
}

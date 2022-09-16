package com.kimcompany.jangbogbackendver2.Cart.Dto;

import com.kimcompany.jangbogbackendver2.Cart.Model.CartEntity;
import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import com.kimcompany.jangbogbackendver2.ProductEvent.Model.ProductEventEntity;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SelectListDto {

    private long id;
    private boolean eventFlag;
    private boolean eventExpireFlag=false;
    private int count;
    private long productId;
    private String productName;
    private long  eventId;
    private String eventName;
    private String totalPrice;
    private String price;


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
            this.price=productEventEntity.getEventPrice();
        }else{
            eventFlag=false;
            eventId=0;
            eventName=null;
            this.price = productEntity.getPrice();
        }
        int priceInt = Integer.parseInt(price.replace(",", ""));
        totalPrice= UtilService.confirmPrice(priceInt*count);
    }
}

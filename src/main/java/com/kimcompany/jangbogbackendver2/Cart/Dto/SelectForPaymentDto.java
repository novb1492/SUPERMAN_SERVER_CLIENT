package com.kimcompany.jangbogbackendver2.Cart.Dto;

import com.kimcompany.jangbogbackendver2.Cart.Model.CartEntity;
import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import com.kimcompany.jangbogbackendver2.ProductEvent.Model.ProductEventEntity;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SelectForPaymentDto {

    private CartEntity cartEntity;
    private ProductEntity productEntity;
    private ProductEventEntity productEventEntity;
    private StoreEntity storeEntity;

    @QueryProjection
    public SelectForPaymentDto(CartEntity cartEntity, ProductEntity productEntity, ProductEventEntity productEventEntity, StoreEntity storeEntity) {
        this.cartEntity = cartEntity;
        this.productEntity = productEntity;
        this.productEventEntity = productEventEntity;
        this.storeEntity = storeEntity;
    }
}

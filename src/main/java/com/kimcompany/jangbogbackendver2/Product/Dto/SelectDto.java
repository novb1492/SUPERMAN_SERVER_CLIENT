package com.kimcompany.jangbogbackendver2.Product.Dto;

import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SelectDto {

    private long id;
    private String origin;
    private String imgPath;
    private int state;
    private String price;
    private String name;
    private boolean event;
    private long eventId;
    private String eventName;

    @QueryProjection
    public SelectDto(ProductEntity productEntity) {
        this.id = productEntity.getId();
        this.name = productEntity.getName();
        this.state = productEntity.getCommonColumn().getState();
        this.imgPath = productEntity.getProductImgPath();
        this.price = productEntity.getPrice();
        this.origin = productEntity.getOrigin();
    }
}

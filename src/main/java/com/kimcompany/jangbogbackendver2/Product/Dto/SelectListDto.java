package com.kimcompany.jangbogbackendver2.Product.Dto;

import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SelectListDto {

    private long id;
    private String name;
    private int state;
    private String imgPath;
    private String price;

    @QueryProjection
    public SelectListDto(ProductEntity productEntity) {
        this.id = productEntity.getId();
        this.name = productEntity.getName();
        this.state = productEntity.getCommonColumn().getState();
        this.imgPath = productEntity.getProductImgPath();
        this.price = productEntity.getPrice();
    }
}

package com.kimcompany.jangbogbackendver2.Product.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SearchCondition {

    private int page;
    private String categoryId;
    private String value;

    public SearchCondition(int page, String categoryId, String value) {
        this.page = page;
        this.categoryId = categoryId;
        this.value = value;
    }
}

package com.kimcompany.jangbogbackendver2.Company.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SearchCondition {

    private String category;
    private String keyword;
    private int page;

    public static SearchCondition set(int page,String keyword,String category){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setPage(page);
        searchCondition.setCategory(category);
        searchCondition.setKeyword(keyword);
        return searchCondition;
    }

}

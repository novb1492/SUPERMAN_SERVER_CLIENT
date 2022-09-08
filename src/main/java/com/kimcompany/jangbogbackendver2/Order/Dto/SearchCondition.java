package com.kimcompany.jangbogbackendver2.Order.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SearchCondition {

    private long storeId;
    private String category;
    private String keyword;
    private int state;
    private String periodFlag;
    private String startDate;
    private String endDate;
    private int page;

    public static SearchCondition set(long storeId,String category,String keyword,int state,String periodFlag,String startDate,String endDate,int page){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setCategory(category);
        searchCondition.setKeyword(keyword);
        searchCondition.setStoreId(storeId);
        searchCondition.setState(state);
        searchCondition.setPeriodFlag(periodFlag);
        searchCondition.setPage(page);
        if(periodFlag.equals("true")){
            searchCondition.setStartDate(startDate.replace("T"," "));
            searchCondition.setEndDate(endDate.replace("T"," "));
        }
        return searchCondition;
    }

}

package com.kimcompany.jangbogbackendver2.Employee.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SearchCondition {
    private int page;
    private int pageSize;
    private long storeId;

    public static SearchCondition set(int page,int pageSize,long storeId){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setPage(page);
        searchCondition.setPageSize(pageSize);
        searchCondition.setStoreId(storeId);
        return searchCondition;
    }

}

package com.kimcompany.jangbogbackendver2.Product.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@NoArgsConstructor
@Data
public class SearchCondition {

    private String addr;
    private String name;
    private int page;
    private long categoryId;
    private String keyword;

    public SearchCondition(String addr, String name, HttpServletRequest request) {
        this.addr = addr;
        this.name = name;
        this.page = Integer.parseInt(request.getParameter("page"));
        this.categoryId = Long.parseLong(request.getParameter("category"));
        this.keyword = request.getParameter("keyword");
    }

    private long storeId;
    private int pageSize;

}

package com.kimcompany.jangbogbackendver2.Cart.Dto;

import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.cartSize;

@NoArgsConstructor
@Data
public class SearchCondition {

    private int page;
    private long userId;
    private int pageSize;

    public SearchCondition(int page) {
        this.page = page;
        this.userId = UtilService.getLoginUserId();
        this.pageSize= cartSize;
    }
}

package com.kimcompany.jangbogbackendver2.Product.Repo;

import com.kimcompany.jangbogbackendver2.Product.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Product.Dto.SelectListDto;
import org.springframework.data.domain.Page;

public interface ProductRepoCustom {
    Boolean exist(long storeId, String productName);
    public Page<SelectListDto> selectForList(int pageSize, long storeId, SearchCondition searchCondition);


}

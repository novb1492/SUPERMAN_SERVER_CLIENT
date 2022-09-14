package com.kimcompany.jangbogbackendver2.Cart.Repo;


import com.kimcompany.jangbogbackendver2.Cart.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Cart.Dto.SelectListDto;
import org.springframework.data.domain.Page;

public interface CartRepoCustom {

    public Page<SelectListDto> selectForList(SearchCondition searchCondition);


}

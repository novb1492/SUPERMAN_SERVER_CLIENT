package com.kimcompany.jangbogbackendver2.Cart.Repo;


import com.kimcompany.jangbogbackendver2.Cart.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Cart.Dto.SelectForPaymentDto;
import com.kimcompany.jangbogbackendver2.Cart.Dto.SelectListDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CartRepoCustom {

    public Page<SelectListDto> selectForList(SearchCondition searchCondition);
    public Optional<SelectForPaymentDto> selectForPayment(Long cartId, int trueState, Long userId);


}

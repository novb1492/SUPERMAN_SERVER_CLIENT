package com.kimcompany.jangbogbackendver2.Cart.Service;

import com.kimcompany.jangbogbackendver2.Cart.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Cart.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Cart.Repo.CartRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CartSelectService {
    private final CartRepo cartRepo;

    public Page<SelectListDto>selectForList(SearchCondition searchCondition){
        return cartRepo.selectForList(searchCondition);
    }

}

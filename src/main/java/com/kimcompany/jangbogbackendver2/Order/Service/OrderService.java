package com.kimcompany.jangbogbackendver2.Order.Service;

import com.kimcompany.jangbogbackendver2.Order.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Order.Dto.SelectDto;
import com.kimcompany.jangbogbackendver2.Order.Dto.SelectDtoAndCard;
import com.kimcompany.jangbogbackendver2.Order.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Payment.Dto.SelectForOrderDto;
import com.kimcompany.jangbogbackendver2.Payment.Service.CardSelectService;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.deleteState;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderSelectService orderSelectService;
    private final CardSelectService cardSelectService;

    public Page<SelectListDto>selectForList(SearchCondition searchCondition){
        if(searchCondition.getState()== deleteState){
            throw new IllegalArgumentException("조회 할 수 없는 주문입니다");
        }
        return orderSelectService.selectForList(searchCondition);
    }
    public SelectDtoAndCard selectForDetail(long storeId, long cardId){
        SelectForOrderDto selectForOrderDto = cardSelectService.selectForOrder(storeId, cardId).orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 거래 내역입니다"));
        return new SelectDtoAndCard(orderSelectService.selectForDetail(storeId, cardId),selectForOrderDto);
    }
}

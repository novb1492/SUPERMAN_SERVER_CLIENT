package com.kimcompany.jangbogbackendver2.Cart.Service;

import com.kimcompany.jangbogbackendver2.Cart.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Cart.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Cart.Repo.CartRepo;
import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import com.kimcompany.jangbogbackendver2.Product.Repo.ProductRepo;
import com.kimcompany.jangbogbackendver2.ProductEvent.Model.ProductEventEntity;
import com.kimcompany.jangbogbackendver2.ProductEvent.Repo.ProductEventRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.deleteState;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.trueStateNum;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CartSelectService {
    private final CartRepo cartRepo;
    private final ProductEventRepo productEventRepo;
    private final ProductRepo productRepo;

    public Page<SelectListDto>selectForList(SearchCondition searchCondition){
        Page<SelectListDto> selectListDtos = cartRepo.selectForList(searchCondition);
        String price=null;
        /*
            장바구니 제품별 이벤트가 있다면 해당이벤트가 아직 유효한지검사
         */
        for(SelectListDto s:selectListDtos.getContent()){
            if(s.getEventId()!=0){
                ProductEventEntity productEventEntity= productEventRepo.findProductIdAndId(s.getProductId(), Timestamp.valueOf(LocalDateTime.now()), s.getEventId(), trueStateNum).orElseGet(() ->null);
                if(productEventEntity!=null){
                    price = productEventEntity.getEventPrice();
                }else{
                    s.setEventExpireFlag(true);
                    ProductEntity productEntity = productRepo.findIdWithEntity(s.getProductId(), deleteState).orElseGet(()->null);
                    if(productEntity!=null){
                        price = productEntity.getPrice();
                    }
                }
                s.setPrice(price);
            }
        }
        return selectListDtos;
    }

}

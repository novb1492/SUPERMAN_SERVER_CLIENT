package com.kimcompany.jangbogbackendver2.Cart.Service;

import com.kimcompany.jangbogbackendver2.Cart.Dto.TryInsertDto;
import com.kimcompany.jangbogbackendver2.Cart.Model.CartEntity;
import com.kimcompany.jangbogbackendver2.Cart.Repo.CartRepo;
import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Member.Model.ClientEntity;
import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import com.kimcompany.jangbogbackendver2.Product.Repo.ProductRepo;
import com.kimcompany.jangbogbackendver2.ProductEvent.Model.ProductEventEntity;
import com.kimcompany.jangbogbackendver2.ProductEvent.Repo.ProductEventRepo;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.deleteState;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.trueStateNum;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepo cartRepo;
    private final ProductEventRepo productEventRepo;
    private final ProductRepo productRepo;

    @Transactional
    public void save(TryInsertDto tryInsertDto){
        Long eventId = tryInsertDto.getEventId();
        long productId = tryInsertDto.getProductId();
        String price=null;
        System.out.println("ee:"+eventId);
        ProductEntity productEntity = productRepo.findIdWithEntity(productId, deleteState).orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 상품입니다"));
        ProductEventEntity productEventEntity = new ProductEventEntity();
        /*
            이벤트번호가 있다면 정상인지 검사
         */
        if(!eventId.equals(0L)) {
            productEventEntity= productEventRepo.findProductIdAndId(productId, Timestamp.valueOf(LocalDateTime.now()), eventId, trueStateNum).orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 이벤트 입니다"));
            price = productEventEntity.getEventPrice();
        }else{
            productEventEntity=null;
            price = productEntity.getPrice();
        }
        CartEntity cartEntity = CartEntity.builder().productEventEntities(productEventEntity)
                .clientEntity(ClientEntity.builder().id(UtilService.getLoginUserId()).build())
                .commonColumn(CommonColumn.set(trueStateNum)).productEntity(ProductEntity.builder().id(productId).build())
                .count(tryInsertDto.getCount())
                .price(price).build();
        cartRepo.save(cartEntity);
    }

}

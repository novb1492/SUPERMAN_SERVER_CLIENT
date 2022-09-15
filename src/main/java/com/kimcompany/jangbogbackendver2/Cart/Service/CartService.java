package com.kimcompany.jangbogbackendver2.Cart.Service;

import com.kimcompany.jangbogbackendver2.Cart.Dto.*;
import com.kimcompany.jangbogbackendver2.Cart.Model.CartEntity;
import com.kimcompany.jangbogbackendver2.Cart.Repo.CartRepo;
import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Member.Model.ClientEntity;
import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import com.kimcompany.jangbogbackendver2.Product.Repo.ProductRepo;
import com.kimcompany.jangbogbackendver2.ProductEvent.Model.ProductEventEntity;
import com.kimcompany.jangbogbackendver2.ProductEvent.Repo.ProductEventRepo;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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
    @Transactional
    public void deleteById(tryDeleteDto tryDeleteDto){
        List<Long> ids = tryDeleteDto.getIds();
        for(Long id:ids){
            cartRepo.updateStateById(deleteState,id,UtilService.getLoginUserId());
        }
    }
    @Transactional
    public void changeCount(TryUpdateCountDto tryUpdateCountDto){
        Integer integer = cartRepo.updateCountById(tryUpdateCountDto.getId(), UtilService.getLoginUserId(), tryUpdateCountDto.getCount());
        if(integer!=1){
            throw new IllegalArgumentException("수량변경에 실패했습니다");
        }
    }

    public void makePaymentInfo(TryPaymentDto tryPaymentDto){
        List<Map<String, Object>> payments = tryPaymentDto.getPayments();
        Map<Long, String> storeDeliverPrices = new HashMap<>();
        Map<Long, Integer> totalPriceByStores = new HashMap<>();
        for(Map<String,Object>payment:payments){
            Long cartId =Long.parseLong(Optional.ofNullable(payment.get("cartId")).orElseThrow(()->new IllegalArgumentException("카트 아이디값이 없습니다")).toString());
            int count = Integer.parseInt(Optional.ofNullable(payment.get("count")).orElseThrow(() -> new IllegalArgumentException("주문수량이 없습니다")).toString());
            if(count<=0){
                throw new IllegalArgumentException("최소 구매가능 수량은 1개입니다");
            }
            SelectForPaymentDto selectForPaymentDto = cartRepo.selectForPayment(cartId, trueStateNum, UtilService.getLoginUserId()).orElseThrow(() -> new IllegalArgumentException("장바구니에서 해당 제품을 찾을 수 없습니다"));
            /*
                메장 영업시간인지 검증
             */
            StoreEntity storeEntity = selectForPaymentDto.getStoreEntity();
            confirmStoreTime(storeEntity.getOpenTime(), storeEntity.getCloseTime());
            /*
                가격 만들기
             */
            ProductEntity productEntity = selectForPaymentDto.getProductEntity();
            int price = Integer.parseInt(productEntity.getPrice().replace(",",""));
            ProductEventEntity productEventEntity = selectForPaymentDto.getProductEventEntity();
            if(productEventEntity!=null){
                /*
                    적용되는 이벤트가 있다면 해당일인지 확인하고 가격적용
                */
                if (LocalDateTime.now().isAfter(productEventEntity.getStartDate().toLocalDateTime())&&LocalDateTime.now().isBefore(productEventEntity.getEndDate().toLocalDateTime())){
                    price = Integer.parseInt(productEventEntity.getEventPrice().replace(",", ""));
                }
            }
            /*
                매장별로 주문금액 담기
             */
            long storeId = storeEntity.getId();
            int priceAndCount = price * count;
            int totalPrice = Optional.ofNullable(totalPriceByStores.get(storeId)).orElseGet(()->0);
            totalPriceByStores.put(storeId, totalPrice + priceAndCount);
            /*
                매장별 배달가능 금액 저장
             */
            if(!storeDeliverPrices.containsKey(storeId)){
                storeDeliverPrices.put(storeId, storeEntity.getMinOrderPrice()+","+storeEntity.getName());
            }
        }
        /*
            매장별 최소금액 초과하는지 검사
         */
        for(Map.Entry<Long, String> storeDeliverPrice:storeDeliverPrices.entrySet()){
            String[] val = storeDeliverPrice.getValue().split(",");
            if(totalPriceByStores.get(storeDeliverPrice.getKey())<Integer.parseInt(val[0])){
                throw new IllegalArgumentException("매장 최소 주문 금액을 못 맞춘 주문이 있습니다 매장:" + val[1] + "최소주문금액:" + val[0] + "주문금액:" + totalPriceByStores.get(storeDeliverPrice.getKey()));
            }
        }
    }

    private void confirmStoreTime(String  storeOpenTime,String storeCloseTime){
        String[] storeOpenTimes = storeOpenTime.split(":");
        String[] storeCloseTimes = storeCloseTime.split(":");
        LocalDateTime openLocalDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(Integer.parseInt(storeOpenTimes[0]), Integer.parseInt(storeOpenTimes[1])));
        LocalDateTime closeLocalDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(Integer.parseInt(storeCloseTimes[0]), Integer.parseInt(storeCloseTimes[1])));

        if(LocalDateTime.now().isBefore(openLocalDateTime)){
            throw new IllegalArgumentException("매장 오픈전 입니다 오픈시간: "+storeOpenTime);
        }else if(LocalDateTime.now().isAfter(closeLocalDateTime)){
            throw new IllegalArgumentException("매장 마감시간입니다 오픈시간: "+storeOpenTime+" 종료시간:"+storeCloseTime);
        }

    }
}

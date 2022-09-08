package com.kimcompany.jangbogbackendver2.Payment.Service;

import com.kimcompany.jangbogbackendver2.Api.Kg.Dto.RequestCancelPartialDto;
import com.kimcompany.jangbogbackendver2.Api.Kg.Service.KgService;
import com.kimcompany.jangbogbackendver2.Employee.EmployeeSelectService;
import com.kimcompany.jangbogbackendver2.Order.Dto.RefundDto;
import com.kimcompany.jangbogbackendver2.Order.Dto.TryRefundDto;
import com.kimcompany.jangbogbackendver2.Order.Model.OrderEntity;
import com.kimcompany.jangbogbackendver2.Order.Repo.OrderRepo;
import com.kimcompany.jangbogbackendver2.Order.Service.OrderSelectService;
import com.kimcompany.jangbogbackendver2.Payment.Model.CardEntity;
import com.kimcompany.jangbogbackendver2.Payment.Repo.CardRepo;
import com.kimcompany.jangbogbackendver2.Store.StoreSelectService;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Util.EtcService;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.*;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.cantFindStoreMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final KgService kgService;
    private final OrderSelectService orderSelectService;
    private final EtcService etcService;
    private final OrderRepo orderRepo;
    private final CardRepo cardRepo;
    private final CardSelectService cardSelectService;


    @Transactional(rollbackFor = Exception.class)
    public void refundAll(long cardId) throws NoSuchAlgorithmException, SQLException {
        CardEntity cardEntity = cardRepo.findByIdForRefundAll(cardId, deleteState).orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 결제 내역입니다"));
        long storeId = cardEntity.getCommonPaymentEntity().getStoreEntity().getId();
        etcService.confirmOwn(storeId);
        if(orderRepo.updateAfterRefundCheck(refundAllNum, 0, cardId, storeId)!=1){
            throw new SQLException(failOrderUpdateMessage);
        }
        if(cardRepo.updateState(deleteState, refundAllNum, cardId, storeId)!=1){
            throw new SQLException(failCardUpdateMessage);
        }
        RequestCancelPartialDto dto = RequestCancelPartialDto.set("매장에서 직접환불", cardEntity.getCommonPaymentEntity().getPTid());
        JSONObject jsonObject = kgService.cancelAllService(dto);
        if(!jsonObject.get("resultCode").equals("00")){
            String msg = jsonObject.get("resultMsg").toString();
            if(msg.equals("부분취소 원거래 취소불가")){
                log.info("부분취소 원거래 취소불가로 인해 자동 부분취소 요청 시작");
                RequestCancelPartialDto dto2 = RequestCancelPartialDto.set("매장에서 직접환불", cardEntity.getCommonPaymentEntity().getPTid()
                        , Integer.toString(cardEntity.getCommonPaymentEntity().getPrtcRemains()), "0", PartialRefundText);
                JSONObject jsonObject2 = kgService.cancelAllService(dto2);
                if(!jsonObject2.get("resultCode").equals("00")){
                    throw new IllegalArgumentException("환불에 실패했습니다 사유:" + jsonObject2.get("resultMsg"));
                }
                return;
            }
            throw new IllegalArgumentException("환불에 실패했습니다 사유:" + jsonObject.get("resultMsg"));
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void refund(TryRefundDto tryRefundDto) throws NoSuchAlgorithmException, SQLException {
        log.info("부분취소");
        long orderId = Long.parseLong(tryRefundDto.getOrderId());
        RefundDto refundDto = orderSelectService.selectForRefund(orderId).orElseThrow(() -> new IllegalArgumentException("찾을 수없는 주문정보 입니다"));
        if(refundDto.getOrderEntity().getCommonColumn().getState()!= trueStateNum){
            throw new IllegalArgumentException("이미 환불된 상품입니다");
        }
        long cardId = refundDto.getCardEntity().getId();
        long storeId = refundDto.getOrderEntity().getStoreEntity().getId();
        etcService.confirmOwn(storeId);
        int totalCount = refundDto.getOrderEntity().getTotalCount();
        int requestCount = tryRefundDto.getCount();
        int newCount=confirmCount(requestCount,totalCount);
        int cancelPrice= tryRefundDto.getCount()*refundDto.getOrderEntity().getPrice();
        int cardPrice = refundDto.getCardEntity().getCommonPaymentEntity().getPrtcRemains();
        confirmPrice(cancelPrice,refundDto.getOrderEntity().getPrice()*refundDto.getOrderEntity().getTotalCount());
        int newPrice=confirmPriceAll(cancelPrice,cardPrice);
        log.info("취소요청 금액:{},원금액:{},남은금액:{}",cancelPrice,cardPrice,cardPrice-cancelPrice);
        int state= refundNum;
        if(newCount>=1){
            state= trueStateNum;
        }
        if(orderRepo.updateAfterRefund(state, newCount, orderId,storeId)!=1){
            throw new SQLException(failOrderUpdateMessage);
        }
        if(cardRepo.updateAfterRefund(newPrice,cardId,storeId,refundDto.getCardEntity().getCommonPaymentEntity().getPrtcCnt()+1)!=1){
            throw new SQLException(failCardUpdateMessage);
        }
        RequestCancelPartialDto dto =RequestCancelPartialDto.set("매장에서 직접환불",refundDto.getCardEntity().getCommonPaymentEntity().getPTid()
                , Integer.toString(cancelPrice), Integer.toString(cardPrice-cancelPrice), PartialRefundText);
        JSONObject jsonObject = kgService.cancelAllService(dto);
        if(!jsonObject.get("resultCode").equals("00")){
            throw new IllegalArgumentException("환불에 실패했습니다 사유:" + jsonObject.get("resultMsg"));
        }
        /*
            환불 요청 완료후
            해당 결제번호 소속 주문 상태 검사 및 후처리
         */
        afterRefund(storeId,cardId);
    }
    @Transactional
    public void afterRefund(long storeId,long cardId){
        /*
            해당결제번호의 모든 주문 조회
         */
        List<Integer> states = orderSelectService.selectForAfterRefund(storeId, cardId);
        if(states.isEmpty()){
            return;
        }
        boolean flag=true;
        /*
            해당 결제 번호 모든 소속 주문 상태확인
         */
        for(int state:states){
            if(state!= refundNum){
                flag=false;
                break;
            }
        }
        if(flag){
            /*
                해당 결제번호 소속 주문이 모두 환불되었다면
                결제상태 변경
             */
            orderRepo.updateAfterRefundCheck(refundAllNum, 0, cardId, storeId);
            cardRepo.updateState(deleteState, refundAllNum, cardId, storeId);
        }
    }
    private int confirmPriceAll(int cancelPrice,int cardPrice){
        int newPrice = cardPrice - cancelPrice;
        if(newPrice<0){
            throw new IllegalArgumentException("해당 제품 최대 환불 가능금액은:"+UtilService.confirmPrice(cardPrice)+"원 입니다 " +
                    "\n 요청금액:"+UtilService.confirmPrice(cancelPrice));
        }
       return newPrice;
    }
    private void confirmPrice(int cancelPrice,int price){
        if(cancelPrice>price){
            throw new IllegalArgumentException("해당 제품 최대 환불 가능금액은:"+UtilService.confirmPrice(price)+"원 입니다 " +
                    "\n 요청금액:"+UtilService.confirmPrice(cancelPrice));
        }
    }
    private int  confirmCount(int requestCount,int count){
        int newCount=count-requestCount;
        if(newCount<0){
            throw new IllegalArgumentException("해당 제품 최대 환불 가능개수는:"+count+"개 입니다 \n 요청개수:"+requestCount);
        }
        return newCount;
    }
    public Map<String,JSONObject>selectForPeriod(long storeId,int year,int requestMonth){
        LocalDateTime start = LocalDateTime.of(year, requestMonth, 1, 0, 0, 0);
        /*
         월별 말일 구하기
         */
        Calendar cal = Calendar.getInstance();
        cal.set(year,requestMonth-1,1);
        LocalDateTime end = LocalDateTime.of(year, requestMonth, cal.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        List<CardEntity> cardEntities = cardSelectService.selectByStoreIdAndPeriod(storeId, start, end);
        Map<String, JSONObject> payments = new HashMap<>();
        JSONObject days = new JSONObject();
        JSONObject hours = new JSONObject();
        JSONObject dayOfWeeks = new JSONObject();
        for(CardEntity c:cardEntities){
            LocalDateTime localDateTime = c.getCommonColumn().getCreated();
            int day = localDateTime.getDayOfMonth();
            int hour = localDateTime.getHour();
            DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
            int dayOfWeekInt = dayOfWeek.getValue();
            long price = c.getCommonPaymentEntity().getPrtcRemains();
            if(days.containsKey(day)){
                long dayPrice = Long.parseLong(days.get(day).toString());
                dayPrice += price;
                days.put(day, dayPrice);
            }else{
                days.put(day, price);
            }
            if(hours.containsKey(hour)){
                long hourPrice= Long.parseLong(hours.get(hour).toString());
                hourPrice += price;
                hours.put(hour, hourPrice);
            }else{
                hours.put(hour, price);
            }
            if(dayOfWeeks.containsKey(dayOfWeekInt)){
                long hourPrice= Long.parseLong(dayOfWeeks.get(dayOfWeekInt).toString());
                hourPrice += price;
                dayOfWeeks.put(dayOfWeekInt, hourPrice);
            }else{
                dayOfWeeks.put(dayOfWeekInt, price);
            }
        }
        payments.put("days",days);
        payments.put("hours",hours);
        payments.put("dayOfWeeks",dayOfWeeks);
        return payments;
    }

}

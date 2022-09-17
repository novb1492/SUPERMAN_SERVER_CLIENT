package com.kimcompany.jangbogbackendver2.Payment;

import com.kimcompany.jangbogbackendver2.Api.Kg.Dto.CardResultDto;
import com.kimcompany.jangbogbackendver2.Api.Kg.Dto.RequestCancelPartialDto;
import com.kimcompany.jangbogbackendver2.Api.Kg.Service.KgService;
import com.kimcompany.jangbogbackendver2.Common.AddressColumn;
import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Member.Model.ClientEntity;
import com.kimcompany.jangbogbackendver2.Order.Dto.TryRefundDto;
import com.kimcompany.jangbogbackendver2.Order.Model.OrderEntity;
import com.kimcompany.jangbogbackendver2.Order.Repo.OrderRepo;
import com.kimcompany.jangbogbackendver2.Payment.Model.CardEntity;
import com.kimcompany.jangbogbackendver2.Payment.Model.CommonPaymentEntity;
import com.kimcompany.jangbogbackendver2.Payment.Repo.CardRepo;
import com.kimcompany.jangbogbackendver2.Payment.Service.PaymentService;
import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import com.kimcompany.jangbogbackendver2.ProductEvent.Model.ProductEventEntity;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.simple.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final KgService KgService;
    private final CardRepo cardRepo;
    private final PaymentService paymentService;
    private final RedisTemplate<String,Object> redisTemplate;
    private final OrderRepo orderRepo;

    /**
     * 어드민 페이지 환불요청 테스트위해 임시 구축한 결제 api컨트롤러
     * @param request
     * @param response
     */
    @RequestMapping(value = "/kg", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public void kgtest(HttpServletRequest request, HttpServletResponse response) {
        String P_STATUS = request.getParameter("P_STATUS");       // 인증 상태
        String P_RMESG1 = request.getParameter("P_RMESG1");      // 인증 결과 메시지
        String P_TID = request.getParameter("P_TID");                   // 인증 거래번호
        String P_REQ_URL = request.getParameter("P_REQ_URL");    // 결제요청 URL
        String P_NOTI = request.getParameter("P_NOTI");              // 기타주문정보

        String P_MID = "INIpayTest";
        log.info("status:{}", P_STATUS);
        // 승인요청을 위한 P_MID, P_TID 세팅


        if (P_STATUS.equals("01")) { // 인증결과가 실패일 경우
            log.info("Auth Fail");
            log.info("<br>");
            log.info(P_RMESG1);
        }// STEP2 에 이어 인증결과가 성공일(P_STATUS=00) 경우 STEP2 에서 받은 인증결과로 아래 승인요청 진행


        else {

            // 승인요청할 데이터
            P_REQ_URL = P_REQ_URL + "?P_TID=" + P_TID + "&P_MID=" + P_MID;


            GetMethod method = new GetMethod(P_REQ_URL);
            HttpClient client = new HttpClient();
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3, false));

            HashMap<String, String> map = new HashMap<String, String>();

            try {
                int statusCode = client.executeMethod(method);

                if (statusCode != HttpStatus.SC_OK) {
                    log.info("Method failed: {}", method.getStatusLine());

                }


// -------------------- 승인결과 수신 -------------------------------------------------

                byte[] responseBody = method.getResponseBody();
                String[] values = new String(responseBody).split("&");
                CardResultDto cardResultDto = CardResultDto.set(values);
                String oid = cardResultDto.getP_OID();
                LinkedHashMap<String, Object> orderInfos = (LinkedHashMap<String, Object>) redisTemplate.opsForHash().entries(oid + "payment").get(oid + "payment");
                System.out.println(orderInfos.toString());
                if(!orderInfos.get("confirmTotalPrice").toString().equals(cardResultDto.getPAmt())){
                    throw new Exception("금액 불일치 ");
                }
                Map<String, List<Map<String, Object>>> orderInfo = (Map<String, List<Map<String, Object>>>) orderInfos.get("paymentInfo");
                for(Map.Entry<String, List<Map<String, Object>>> order:orderInfo.entrySet()){
                    System.out.println( order.getKey());
                    boolean saveCardFlag = false;
                    List<Map<String, Object>> orderDetail = order.getValue();
                    CardEntity cardEntity = new CardEntity();
                    long storeId = Long.parseLong(order.getKey());
                    int index=0;
                    for(Map<String, Object> o:orderDetail){
                        if(index==orderDetail.size()-1){
                            continue;
                        }
                        if(!saveCardFlag){
                            cardEntity = CardResultDto.dtoToEntity(cardResultDto, storeId, Integer.parseInt(orderDetail.get(orderDetail.size()-1).get("totalPrice").toString()));
                            cardRepo.save(cardEntity);
                            saveCardFlag=true;
                        }
                        String[] address = o.get("address").toString().split(",");
                        ProductEventEntity productEventEntity=null;
                        if(!UtilService.confirmNull(o.get("eventId").toString())&&!o.get("eventId").toString().equals("0")){
                            productEventEntity = ProductEventEntity.builder().id(Long.parseLong(o.get("eventId").toString())).build();
                        }
                        OrderEntity orderEntity = OrderEntity.builder().cardEntity(cardEntity)
                                .clientEntity(ClientEntity.builder().id(Long.parseLong(cardResultDto.getPUserName())).build())
                                .commonColumn(CommonColumn.builder().state(BasicText.trueStateNum).build())
                                .addressColumn(AddressColumn.builder().address(address[1]).detailAddress(address[2]).postCode(address[0]).build())
                                .price(Integer.parseInt(o.get("price").toString()))
                                .storeEntity(StoreEntity.builder().id(storeId).build())
                                .productEntity(ProductEntity.builder().id(Long.parseLong(o.get("productId").toString())).build())
                                .totalCount(Integer.parseInt(o.get("count").toString()))
                                .productEventEntity(productEventEntity)
                                .build();
                        orderRepo.save(orderEntity);
                        index+=1;
                    }

                }
            } catch (HttpException e) {
                log.info("Fatal protocol violation: {}", e.getMessage());
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
                log.info("Fatal protocol violation: {}", e.getMessage());
            } finally {
                method.releaseConnection();
            }
            UtilService.goForward( "http://localhost:3000/test?tid="+P_TID,request,response);
        }
    }

    /**
     * 부분 취소 요청
     * @param cardId
     * @return
     * @throws NoSuchAlgorithmException
     */
    @RequestMapping(value = "/payment/cancle/all/{cardId}",method = RequestMethod.POST)
    public ResponseEntity<?> fail(@PathVariable String cardId) throws NoSuchAlgorithmException, SQLException {
        paymentService.refundAll(Long.parseLong(cardId));
        JSONObject response = new JSONObject();
        response.put("message", "결제번호:" + cardId + "가 전부 환불 되었습니다");
        return ResponseEntity.ok().body(response);
    }

    /**
     * 전체환불 요청
     * @param refundDto
     * @return
     * @throws NoSuchAlgorithmException
     * @throws SQLException
     */
    @RequestMapping(value = "/payment/cancle",method = RequestMethod.POST)
    public ResponseEntity<?> fail2(@Valid @RequestBody TryRefundDto refundDto) throws NoSuchAlgorithmException, SQLException {
        paymentService.refund(refundDto);
        JSONObject response = new JSONObject();
        response.put("message", "주문번호:" + refundDto.getOrderId() + "가 환불 되었습니다");
        return ResponseEntity.ok().body(response);
    }
    @RequestMapping(value = "/payment/chart/{year}/{month}/{storeId}",method = RequestMethod.GET)
    public ResponseEntity<?> selectForChart(@PathVariable("year")String year,@PathVariable("month")String month,@PathVariable("storeId")String storeId) {
        long storeIdLong = Long.parseLong(storeId);
        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);
        return ResponseEntity.ok().body(paymentService.selectForPeriod(storeIdLong, yearInt, monthInt));
    }
}

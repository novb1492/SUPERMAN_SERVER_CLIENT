package com.kimcompany.jangbogbackendver2.Payment;

import com.kimcompany.jangbogbackendver2.Api.Kg.Dto.CardResultDto;
import com.kimcompany.jangbogbackendver2.Api.Kg.Dto.RequestCancelPartialDto;
import com.kimcompany.jangbogbackendver2.Api.Kg.Service.KgService;
import com.kimcompany.jangbogbackendver2.Order.Dto.TryRefundDto;
import com.kimcompany.jangbogbackendver2.Payment.Model.CardEntity;
import com.kimcompany.jangbogbackendver2.Payment.Model.CommonPaymentEntity;
import com.kimcompany.jangbogbackendver2.Payment.Repo.CardRepo;
import com.kimcompany.jangbogbackendver2.Payment.Service.PaymentService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final KgService KgService;
    private final CardRepo cardRepo;
    private final PaymentService paymentService;

    /**
     * 어드민 페이지 환불요청 테스트위해 임시 구축한 결제 api컨트롤러
     * @param request
     * @param response
     */
    @RequestMapping(value = "/kg", method = RequestMethod.POST)
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
                cardRepo.save(CardResultDto.dtoToEntity(cardResultDto));
            } catch (HttpException e) {
                log.info("Fatal protocol violation: {}", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                log.info("Fatal transport error: {}", e.getMessage());
                e.printStackTrace();
            } finally {
                method.releaseConnection();
            }
            UtilService.goForward( "https://localhost:3030/html/test?tid="+P_TID,request,response);
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

package com.kimcompany.jangbogbackendver2.Api.Kg.Dto;

import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.PartialRefundText;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Slf4j
public class RequestCancelPartialDto {

    private RequestCancelDto requestCancelDto;
    private String price;// 부분취소만 해당 요청금액
    private String confirmPrice;//부분취소만 해당 남은 금액

    public static RequestCancelDto setRequestCancelDto(String method, String tid, String msg, String type){
        return RequestCancelDto.builder().clientIp(PropertiesText.serverIp).method(method).mid(PropertiesText.mid).tid(tid).msg(msg)
                .type(type).timestamp(getTimeStamp()).build();
    }
    public static String getTimeStamp(){
        Date date_now = new Date(System.currentTimeMillis()); // 현재시간을 가져와 Date형으로 저장한다//년월일시분초 14자리 포멧
        SimpleDateFormat fourteen_format = new SimpleDateFormat("yyyyMMddHHmmss");
        return fourteen_format.format(date_now);
    }
    public static String getDataHash(RequestCancelPartialDto requestCancelPartialDto) throws NoSuchAlgorithmException {
        RequestCancelDto requestCancelDto1=requestCancelPartialDto.getRequestCancelDto();
        String data_hash = PropertiesText.kgKey + requestCancelDto1.getType() + requestCancelDto1.getMethod() + requestCancelDto1.getTimestamp() + PropertiesText.serverIp + PropertiesText.mid + requestCancelDto1.getTid() + requestCancelPartialDto.getPrice() + requestCancelPartialDto.getConfirmPrice();
        log.info("해시문자열: {}", data_hash);
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(data_hash.getBytes());
        return String.format("%0128x", new BigInteger(1, md.digest()));
    }
    public static MultiValueMap<String,Object> dtoToBody(RequestCancelPartialDto requestCancelPartialDto){
        RequestCancelDto requestCancelDto1=requestCancelPartialDto.getRequestCancelDto();
        MultiValueMap<String, Object> CashReceiptsMap = new LinkedMultiValueMap<>();
        CashReceiptsMap.add("type"       ,requestCancelDto1.getType());
        CashReceiptsMap.add("paymethod"  ,requestCancelDto1.getMethod());
        CashReceiptsMap.add("timestamp"  ,requestCancelDto1.getTimestamp());
        CashReceiptsMap.add("clientIp"   ,requestCancelDto1.getClientIp());
        CashReceiptsMap.add("mid"        ,requestCancelDto1.getMid());
        CashReceiptsMap.add("tid"        ,requestCancelDto1.getTid());
        CashReceiptsMap.add("msg"        ,requestCancelDto1.getMsg());
        CashReceiptsMap.add("hashData"   ,requestCancelDto1.getHashData());
        CashReceiptsMap.add("price", requestCancelPartialDto.getPrice());
        CashReceiptsMap.add("confirmPrice", requestCancelPartialDto.getConfirmPrice());
        return CashReceiptsMap;
    }
    public static RequestCancelPartialDto set(String msg,String tid,String price,String confirmPrice,String situation){
        return RequestCancelPartialDto.builder().requestCancelDto(RequestCancelPartialDto.setRequestCancelDto("Card"
                        , tid, msg, situation))
                .price(price).confirmPrice(confirmPrice).build();
    }
    public static RequestCancelPartialDto set(String msg,String pid){
        return RequestCancelPartialDto.builder().requestCancelDto(RequestCancelPartialDto.setRequestCancelDto("Card"
                ,pid, msg, BasicText.RefundText)).build();
    }

}

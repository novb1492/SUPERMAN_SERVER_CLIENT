package com.kimcompany.jangbogbackendver2.Api.Kg.Dto;

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

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Slf4j
public class RequestCancelDto {

    String type;// 부분,전체
    String method;//carc,vbank등
    String timestamp;//현재연월일시간
    String tid;  // 40byte 승인 TID 입력
    String msg;//메세지
    String mid;//kg가 발급해준 상점 번호
    String clientIp;//서버 ip
    String hashData;// kg요구 조건 대로 만든 문자열(해시전,해시후)

    public static MultiValueMap<String,Object> dtoToBody(RequestCancelDto requestCancelDto){
        MultiValueMap<String, Object> CashReceiptsMap = new LinkedMultiValueMap<>();
        CashReceiptsMap.add("type"       ,requestCancelDto.getType());
        CashReceiptsMap.add("paymethod"  ,requestCancelDto.getMethod());
        CashReceiptsMap.add("timestamp"  ,requestCancelDto.getTimestamp());
        CashReceiptsMap.add("clientIp"   ,requestCancelDto.getClientIp());
        CashReceiptsMap.add("mid"        ,requestCancelDto.getMid());
        CashReceiptsMap.add("tid"        ,requestCancelDto.getTid());
        CashReceiptsMap.add("msg"        ,requestCancelDto.getMsg());
        CashReceiptsMap.add("hashData"   ,requestCancelDto.getHashData());
        return CashReceiptsMap;
    }
    public static String getDataHash(RequestCancelDto requestCancelDto) throws NoSuchAlgorithmException {
        String data_hash= PropertiesText.kgKey +requestCancelDto.getType()+requestCancelDto.getMethod()+requestCancelDto.getTimestamp()+ PropertiesText.serverIp +PropertiesText.mid+requestCancelDto.getTid();
        log.info("해시문자열: {}", data_hash);
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(data_hash.getBytes());
        return String.format("%0128x", new BigInteger(1, md.digest()));
    }


}

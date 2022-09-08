package com.kimcompany.jangbogbackendver2.Api.Kg.Service;

import com.kimcompany.jangbogbackendver2.Api.Kg.Dto.RequestCancelDto;
import com.kimcompany.jangbogbackendver2.Api.Kg.Dto.RequestCancelPartialDto;
import com.kimcompany.jangbogbackendver2.Api.RequestTo;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.security.NoSuchAlgorithmException;


@Service
@Slf4j
public class KgService {
    /**
     * 결제 취소 요청시 사용하는 함수입니다
     */
    public JSONObject cancelAllService(RequestCancelPartialDto requestCancelPartialDto) throws NoSuchAlgorithmException {
        //공통 값 꺼내기
        RequestCancelDto requestCancelDto = requestCancelPartialDto.getRequestCancelDto();
        //요청 헤더 세팅
        HttpHeaders httpHeaders = setHeader();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        //부분/전체 취소 body값 세팅
        if(requestCancelDto.getType().equals(BasicText.RefundText)){
            log.info("전체 취소요청");
            //hash값 암호화
            requestCancelDto.setHashData(RequestCancelDto.getDataHash(requestCancelDto));
            body = requestCancelDto.dtoToBody(requestCancelDto);
        }else{
            log.info("부분 취소 요청");
            //hash값 암호화
            requestCancelDto.setHashData(requestCancelPartialDto.getDataHash(requestCancelPartialDto));
            body = RequestCancelPartialDto.dtoToBody(requestCancelPartialDto);
        }
        //통신 요청
        JSONObject response= RequestTo.requestPost(body, PropertiesText.cancelUrl,httpHeaders);
        log.info("통신결과: "+response.toString());
        return response;
    }

    private HttpHeaders setHeader(){
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Accept", "*/*");
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        return headers;
    }

}

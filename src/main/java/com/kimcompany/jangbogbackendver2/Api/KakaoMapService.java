package com.kimcompany.jangbogbackendver2.Api;

import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

import static com.kimcompany.jangbogbackendver2.Text.PropertiesText.kakaoRestKey;

@Service
public class KakaoMapService{

    public JSONObject getAddress(String address) {
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Authorization","KakaoAK "+ kakaoRestKey);
        return  RequestTo.requestGet(null,"https://dapi.kakao.com/v2/local/search/address.json?query="+address,httpHeaders);
    }
    public int getTotalCount(LinkedHashMap<String, Object> linkedHashMap){
        return Integer.parseInt(linkedHashMap.get("total_count").toString());
    }
}

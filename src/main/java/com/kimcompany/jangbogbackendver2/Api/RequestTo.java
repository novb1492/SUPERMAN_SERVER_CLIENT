package com.kimcompany.jangbogbackendver2.Api;

import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.GET;

public class RequestTo {
    public static <T> JSONObject requestPost(T body, String url, HttpHeaders headers) {
        //body+header합치기
        HttpEntity<T> entity=new HttpEntity<>(body,headers);
        RestTemplate restTemplate=new RestTemplate();
        return restTemplate.postForObject(url, entity, JSONObject.class);

    }
    public static <T> JSONObject requestGet(T body,String url,HttpHeaders headers) {
        RestTemplate restTemplate=new RestTemplate();
        HttpEntity<T>entity=new HttpEntity<>(body,headers);
        ResponseEntity<JSONObject> responseEntity=restTemplate.exchange(url, GET,entity,JSONObject.class);
        return responseEntity.getBody();
    }
}

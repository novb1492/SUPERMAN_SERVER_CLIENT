package com.kimcompany.jangbogbackendver2.Api;

import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.json.simple.JSONObject;

import java.util.*;

@RequiredArgsConstructor
public class JungBu {
    public static JSONObject getCompanyNum(String companyNum,String apiKey) {
        //resttemplate통신이 안되서 okhttp3으로 통신
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        //요청형식이 json 배열임
        List<String> arry = new ArrayList<>();
        arry.add(companyNum);
        JSONObject requestBody=new JSONObject();
        requestBody.put("b_no",arry);
        RequestBody body = RequestBody.create(requestBody.toString(),mediaType);
        Request request = new Request.Builder()
                .url("http://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey="+apiKey)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        //결과 탐색
        try {
            Response response = client.newCall(request).execute();
            return UtilService.stringToJson(response.body().string());
        } catch (Exception e) {
            UtilService.ExceptionValue(e.getMessage(), JungBu.class);
            return null;
        }
    }
}

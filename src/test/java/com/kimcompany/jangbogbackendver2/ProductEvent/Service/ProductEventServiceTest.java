package com.kimcompany.jangbogbackendver2.ProductEvent.Service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductEventServiceTest {
    private List<String>dates=new ArrayList<>();

    @Test
    @DisplayName("이벤트 중복확인 목테스트")
    void test(){
        List<Map<String, Object>> events = set("2022-08-15T11:32", "2022-08-17T11:32", "2022-08-15T11:32", "2022-08-16T11:32");
        Assertions.assertThat(confirmSame(events)).isEqualTo(true);
    }
    @Test
    @DisplayName("이벤트 정상 목테스트")
    void test2(){
        List<Map<String, Object>> events = set("2022-08-15T11:32", "2022-08-17T11:32", "2022-08-16T11:32", "2022-08-16T11:32");
        Assertions.assertThat(confirmSame(events)).isEqualTo(true);
    }
    @Test
    @DisplayName("이벤트 날짜 뒤바꿈 테스트")
    void test3(){
        List<Map<String, Object>> events = set("2022-08-15T11:32", "2022-08-15T11:31", "2022-08-16T11:32", "2022-08-16T11:32");
        Assertions.assertThat(confirmDate(events)).isEqualTo(true);
    }
    private  List<Map<String, Object>> set(String startDate,String endDate,String startDate2,String endDate2){
        List<Map<String, Object>> events = new ArrayList<>();
        Map<String,Object>event1=new HashMap<>();
        event1.put("price","13,000");
        event1.put("startDate",startDate);
        event1.put("endDate",endDate);
        event1.put("name","test");
        Map<String,Object>event2=new HashMap<>();
        event2.put("price","15,000");
        event2.put("startDate",startDate2);
        event2.put("endDate",endDate2);
        event2.put("name","test2");
        events.add(event1);
        events.add(event2);
        return events;
    }
    private boolean confirmDate(List<Map<String,Object>>events){
        for(Map<String,Object>event:events){
            String startDate=event.get("startDate").toString().replace("T"," ")+":00";
            String endDate=event.get("endDate").toString().replace("T"," ")+":00";
            System.out.println(startDate+","+endDate);
            if(Timestamp.valueOf(startDate).toLocalDateTime().isAfter(Timestamp.valueOf(endDate).toLocalDateTime())){
                return true;
            }

        }
        return false;
    }
    private boolean confirmSame(List<Map<String,Object>>events){
        List<String>dates=new ArrayList<>();
        for(Map<String,Object>event:events){
            String[]date=event.get("startDate").toString().split("T");
            if(dates.contains(date[0])){
                return true;
            }else{
                dates.add(date[0]);
            }
        }
        return false;
    }
}
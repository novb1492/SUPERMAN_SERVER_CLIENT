package com.kimcompany.jangbogbackendver2.ProductEvent.Service;

import com.kimcompany.jangbogbackendver2.Product.Dto.TryInsertDto;
import com.kimcompany.jangbogbackendver2.Product.Repo.ProductRepo;
import com.kimcompany.jangbogbackendver2.ProductEvent.Model.ProductEventEntity;
import com.kimcompany.jangbogbackendver2.ProductEvent.Repo.ProductEventRepo;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductEventService {

    private final ProductEventRepo productEventRepo;

    @Transactional
    public void save(long productId, List<Map<String,Object>>events){
        if(events.isEmpty()){
            return;
        }
        List<String> dates = new ArrayList<>();
        for(Map<String,Object>event:events){
            if(event.isEmpty()){
                continue;
            }
            confirmDate(event,dates);
            confirmPrice(event);
            confirmName(event);
            productEventRepo.save(TryInsertDto.dtoToEntity(event, productId));
        }
    }
    private void confirmName(Map<String,Object>event){
        if(event.get("name").toString().length()>20){
            throw new IllegalArgumentException("이벤트 이름은 최대 20글자입니다\n"+event.get("name"));
        }
    }
    private void confirmPrice(Map<String,Object>event){
            String price = event.get("price").toString();
            //가격 검증 다시 클라이언트는 숫자만 입력 서버에서 ,추가
        try{
            price=UtilService.confirmPrice(Integer.parseInt(price));
            event.put("price",price);
        }catch (Exception e){
            throw new IllegalArgumentException("잘못된 가격입니다 \n" + price);
        }

    }
    private void confirmDate(Map<String,Object>event,List<String> dates){
            String startDate=event.get("startDate").toString().replace("T"," ")+":00";
            String endDate=event.get("endDate").toString().replace("T"," ")+":00";
            System.out.println(startDate+","+endDate);
            if(!Timestamp.valueOf(startDate).toLocalDateTime().isBefore(Timestamp.valueOf(endDate).toLocalDateTime())){
                throw new IllegalArgumentException("이벤트시작일은 이벤트 종료일보다 빨라야합니다\n"+startDate+","+endDate);
            }
            if(Timestamp.valueOf(startDate).toLocalDateTime().isEqual(Timestamp.valueOf(endDate).toLocalDateTime())){
                throw new IllegalArgumentException("이벤트시작일은 이벤트 종료일보다 빨라야합니다\n"+startDate+","+endDate);
            }
            if(dates.contains(startDate)){
                throw new IllegalArgumentException("동일한 이벤트 시작일이 있습니다\n"+startDate);
            }else {
                dates.add(startDate);
            }
    }
}

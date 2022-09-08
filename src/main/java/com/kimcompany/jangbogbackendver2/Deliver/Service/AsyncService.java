package com.kimcompany.jangbogbackendver2.Deliver.Service;

import com.kimcompany.jangbogbackendver2.Deliver.Model.DeliverDetailEntity;
import com.kimcompany.jangbogbackendver2.Deliver.Model.DeliverEntity;
import com.kimcompany.jangbogbackendver2.Deliver.Repo.DeliverDetailRepo;
import com.kimcompany.jangbogbackendver2.Deliver.Repo.DeliverRepo;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.*;

@Service
@RequiredArgsConstructor
public class AsyncService {
    private final DeliverRepo deliverRepo;
    private final DeliverDetailRepo deliverDetailRepo;

    /**
     * 배달 내역 수정후
     * 모든 배달 내역 확인후 변경
     * @param deliverId
     * @param storeId
     */
    @Async
    @Transactional
    public void checkAfterUpdateDetail(long deliverId,long storeId){
        List<DeliverDetailEntity> deliverDetailEntitys =  deliverDetailRepo.findByDeliverId(deliverId);
        /**
         * 해달배달방 내역중
         * 모든 배달 상세 상태가 trueStateNum가아니면 배달완료 처리
         * 배달 취소/전체쉬소 상태여도 있어도 해당 배달은 배달이 완료된것
         */
        boolean flag=true;
        for(DeliverDetailEntity d:deliverDetailEntitys){
            if(d.getCommonColumn().getState()== trueStateNum||d.getCommonColumn().getState()== deliveringState){
                flag=false;
                break;
            }
        }
        if(flag){
            deliverRepo.updateState(deliverId, deliverDoneState, storeId);
        }
    }

}

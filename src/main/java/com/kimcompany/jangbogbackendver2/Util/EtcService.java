package com.kimcompany.jangbogbackendver2.Util;

import com.kimcompany.jangbogbackendver2.Employee.EmployeeSelectService;
import com.kimcompany.jangbogbackendver2.Store.StoreSelectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.*;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.cantFindStoreMessage;

@Service
@RequiredArgsConstructor
public class EtcService {
    private final StoreSelectService storeSelectService;
    private final EmployeeSelectService employeeSelectService;

    /**
     * 매장 세부사항 접근전
     * 해당 매장 소속인지 검사함수입니다
     * @param storeId
     */
    public void confirmOwn(long storeId){
        long adminId= UtilService.getLoginUserId();
        String role = UtilService.getLoginUserRole();
        confirmOwnCore(storeId,adminId,role);
    }

    /**
     * 웹소캣 에서 사용하기 위한 함수입니다
     * @param storeId
     * @param adminId
     * @param role
     */
    public void confirmOwn(long storeId,long adminId,String role){
        confirmOwnCore(storeId,adminId,role);
    }

    /**
     * 검사 쿼리 던지는 함수입니다
     * @param storeId
     * @param adminId
     * @param role
     */
    private void confirmOwnCore(long storeId,long adminId,String role){
        if(role.equals(ROLE_ADMIN)){
            if(!storeSelectService.checkExist(storeId,adminId)){
                throw new IllegalArgumentException(cantFindStoreMessage);
            }
        }else if(role.equals(ROLE_MANAGE)||role.equals(ROLE_USER)){
            if(!employeeSelectService.exist(storeId, adminId, trueStateNum)){
                throw new IllegalArgumentException(cantFindStoreMessage);
            }
        }
    }
}

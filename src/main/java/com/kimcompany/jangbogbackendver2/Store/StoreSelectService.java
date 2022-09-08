package com.kimcompany.jangbogbackendver2.Store;

import com.kimcompany.jangbogbackendver2.Employee.EmployeeSelectService;
import com.kimcompany.jangbogbackendver2.Store.Dto.*;
import com.kimcompany.jangbogbackendver2.Store.Repo.StoreRepo;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.*;
import static com.kimcompany.jangbogbackendver2.Util.UtilService.getLoginUserId;
import static com.kimcompany.jangbogbackendver2.Util.UtilService.getLoginUserRole;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreSelectService {

    private final StoreRepo storeRepo;
    private final EmployeeSelectService employeeSelectService;

    public boolean checkExist(String address,String storeName){
        return storeRepo.exist(address, storeName);
    }
    public boolean checkExist(long storeId,long adminId){
        return storeRepo.exist(storeId, adminId);
    }

    public Page<SelectRegiDto> selectForRegi(int page,int pageSize){
        return storeRepo.selectForRegi(page, getLoginUserId(), pageSize);
    }
    public Page<SelectRegiDto> selectForRegiManage(int page,int pageSize){
        return storeRepo.selectForRegiManage(page, getLoginUserId(), pageSize);
    }
    public Page<SelectListDto>selectForListAdmin(SearchCondition searchCondition,int pageSize){
        return storeRepo.selectForList(getLoginUserId(),pageSize,searchCondition);
    }
    public Page<SelectListDto>selectForListOther(SearchCondition searchCondition,int pageSize){
        return storeRepo.selectForListOther(getLoginUserId(),pageSize,searchCondition);
    }
    public Optional<SelectInfo>selectStoreInfo(long storeId,long adminId) {
        if (getLoginUserRole().equals(ROLE_ADMIN)) {
            return storeRepo.selectByIdAndAdminId(storeId, adminId);
        }
        if (employeeSelectService.exist(storeId, adminId, trueStateNum)) {
            return storeRepo.selectById(storeId);
        }
        throw new IllegalArgumentException(cantFindStoreMessage);
    }
    public Optional<SelectNotyDto>selectForNoty(long storeId){
        return storeRepo.findByIdForNoty(storeId, deleteState);
    }
}

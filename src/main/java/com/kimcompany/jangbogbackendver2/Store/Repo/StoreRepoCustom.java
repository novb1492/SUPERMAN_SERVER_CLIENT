package com.kimcompany.jangbogbackendver2.Store.Repo;

import com.kimcompany.jangbogbackendver2.Store.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Store.Dto.SelectInfo;
import com.kimcompany.jangbogbackendver2.Store.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Store.Dto.SelectRegiDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface StoreRepoCustom {
    Boolean exist(String address, String storeName);
    Boolean exist(long storeId, long adminId);

    Page<SelectRegiDto> selectForRegi(int page, long adminId,int pageSize);
    Page<SelectListDto>selectForList(long adminId, int pageSize, SearchCondition  searchCondition);
    Page<SelectListDto>selectForListOther(long adminId, int pageSize, SearchCondition  searchCondition);

    Page<SelectRegiDto> selectForRegiManage(int page, long adminId,int pageSize);

    public Optional<SelectInfo> selectByIdAndAdminId(long store, long adminId);

    public Optional<SelectInfo> selectById(long storeId);


}

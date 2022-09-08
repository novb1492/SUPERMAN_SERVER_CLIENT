package com.kimcompany.jangbogbackendver2.Employee.Repo;

import com.kimcompany.jangbogbackendver2.Employee.Dto.NotyEmployeeDto;
import com.kimcompany.jangbogbackendver2.Employee.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Employee.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Store.Dto.InsertEmployNotyDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeCustom {
    public boolean exist(long storeId, long userId,int state);

    public InsertEmployNotyDto selectStoreAndUser(long storeId,long userId);
    public List<NotyEmployeeDto> selectEmployeeByStoreId(long storeId);
    public Page<SelectListDto> selectForList(SearchCondition searchCondition);
}

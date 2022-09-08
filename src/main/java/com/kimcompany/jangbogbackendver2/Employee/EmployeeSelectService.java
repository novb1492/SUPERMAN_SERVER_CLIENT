package com.kimcompany.jangbogbackendver2.Employee;

import com.kimcompany.jangbogbackendver2.Employee.Dto.NotyEmployeeDto;
import com.kimcompany.jangbogbackendver2.Employee.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Employee.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Employee.Repo.EmployeeRepo;
import com.kimcompany.jangbogbackendver2.Store.Dto.InsertEmployNotyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeSelectService {

    private final EmployeeRepo employeeRepo;
    public boolean exist(long storeId,long userId,int state){
        return employeeRepo.exist(storeId, userId,state);
    }

    public InsertEmployNotyDto selectToInsertEmployeeNoty(long storeId,long userId){
        return employeeRepo.selectStoreAndUser(storeId,userId);
    }
    public List<NotyEmployeeDto>selectForNoty(long storeId){
        return employeeRepo.selectEmployeeByStoreId(storeId);
    }

    public Page<SelectListDto>selectForList(SearchCondition searchCondition){
        return employeeRepo.selectForList(searchCondition);
    }

}

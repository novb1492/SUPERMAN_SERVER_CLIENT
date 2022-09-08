package com.kimcompany.jangbogbackendver2.Company.Repo;

import com.kimcompany.jangbogbackendver2.Company.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Company.Dto.SelectListDetailDto;
import org.springframework.data.domain.Page;

public interface CompanyRepoCustom {

    public Page<SelectListDetailDto>selectForList(int pageSize, long adminId, SearchCondition searchCondition);

}

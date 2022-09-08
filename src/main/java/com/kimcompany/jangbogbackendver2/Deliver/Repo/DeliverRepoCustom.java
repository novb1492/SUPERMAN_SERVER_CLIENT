package com.kimcompany.jangbogbackendver2.Deliver.Repo;

import com.kimcompany.jangbogbackendver2.Deliver.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Deliver.Dto.SelectDto;
import com.kimcompany.jangbogbackendver2.Deliver.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Deliver.Model.DeliverDetailEntity;
import com.kimcompany.jangbogbackendver2.Order.Dto.RefundDto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface DeliverRepoCustom {

    public Page<SelectListDto> selectForList(SearchCondition searchCondition);
    public List<SelectDto> selectForDetail(long storeId, long deliverId);
   List<DeliverDetailEntity> selectByDeliverId(long deliverId);
}

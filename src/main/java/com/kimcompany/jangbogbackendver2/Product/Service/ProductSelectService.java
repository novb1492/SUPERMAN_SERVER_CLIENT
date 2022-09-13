package com.kimcompany.jangbogbackendver2.Product.Service;

import com.kimcompany.jangbogbackendver2.Product.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Product.Dto.SelectDto;
import com.kimcompany.jangbogbackendver2.Product.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Product.Repo.ProductRepo;
import com.kimcompany.jangbogbackendver2.ProductEvent.Model.ProductEventEntity;
import com.kimcompany.jangbogbackendver2.ProductEvent.Repo.ProductEventRepo;
import com.kimcompany.jangbogbackendver2.Store.Dto.SelectNotyDto;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import com.kimcompany.jangbogbackendver2.Store.Repo.StoreRepo;
import com.kimcompany.jangbogbackendver2.Store.StoreSelectService;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.deleteState;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.productListPageSize;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductSelectService {
    private final ProductRepo productRepo;
    private final StoreRepo storeRepo;
    private final ProductEventRepo productEventRepo;

    public boolean exist(String productName,long storeId){
        return productRepo.exist(storeId,productName);
    }

    public Page<SelectListDto>selectForList(long storeId, SearchCondition searchCondition){
        return null;
    }
    //------------------------------------------------
    public Page<SelectListDto>selectForList(SearchCondition searchCondition){
        StoreEntity storeEntity = storeRepo.findInsteadOfId(searchCondition.getName(), searchCondition.getAddr(), deleteState).orElseThrow(() -> new IllegalArgumentException("등록되지 않은 매장입니다"));
        searchCondition.setStoreId(storeEntity.getId());
        searchCondition.setPageSize(2);
        Page<SelectListDto> selectListDtos = productRepo.selectForList(searchCondition);
        for(SelectListDto s:selectListDtos.getContent()){
            ProductEventEntity productEventEntity = productEventRepo.findProductId(s.getId(), Timestamp.valueOf(LocalDateTime.now())).orElseGet(() -> null);
            if(productEventEntity!=null){
                s.setPrice(productEventEntity.getEventPrice());
            }
        }

        return selectListDtos;
    }
    public SelectDto selectForId(long id){
        SelectDto selectDto = productRepo.findId(id).orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 상품 입니다"));
        ProductEventEntity productEventEntity = productEventRepo.findProductId(selectDto.getId(), Timestamp.valueOf(LocalDateTime.now())).orElseGet(() -> null);
        if(productEventEntity!=null){
            selectDto.setEvent(true);
            selectDto.setPrice(productEventEntity.getEventPrice());
        }
        return selectDto;
    }

}

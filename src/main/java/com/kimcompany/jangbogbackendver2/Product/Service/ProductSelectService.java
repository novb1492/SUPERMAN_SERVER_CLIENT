package com.kimcompany.jangbogbackendver2.Product.Service;

import com.kimcompany.jangbogbackendver2.Product.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Product.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Product.Repo.ProductRepo;
import com.kimcompany.jangbogbackendver2.Store.Dto.SelectNotyDto;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import com.kimcompany.jangbogbackendver2.Store.Repo.StoreRepo;
import com.kimcompany.jangbogbackendver2.Store.StoreSelectService;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.deleteState;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.productListPageSize;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductSelectService {
    private final ProductRepo productRepo;
    private final StoreRepo storeRepo;

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
        searchCondition.setPageSize(1);
        return productRepo.selectForList(searchCondition);
    }
}

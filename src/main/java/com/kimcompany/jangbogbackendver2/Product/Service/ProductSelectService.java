package com.kimcompany.jangbogbackendver2.Product.Service;

import com.kimcompany.jangbogbackendver2.Product.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Product.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Product.Repo.ProductRepo;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.productListPageSize;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductSelectService {
    private final ProductRepo productRepo;

    public boolean exist(String productName,long storeId){
        return productRepo.exist(storeId,productName);
    }

    public Page<SelectListDto>selectForList(long storeId, SearchCondition searchCondition){
        return productRepo.selectForList(productListPageSize,storeId,searchCondition);
    }
}

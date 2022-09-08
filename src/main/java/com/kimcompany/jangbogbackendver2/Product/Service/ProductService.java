package com.kimcompany.jangbogbackendver2.Product.Service;

import com.kimcompany.jangbogbackendver2.Aws.SqsService;
import com.kimcompany.jangbogbackendver2.Employee.Dto.NotyEmployeeDto;
import com.kimcompany.jangbogbackendver2.Employee.EmployeeSelectService;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Noty.NotyService;
import com.kimcompany.jangbogbackendver2.Product.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Product.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.ProductEvent.Service.ProductEventService;
import com.kimcompany.jangbogbackendver2.ProductKind.Service.ProductKindSelectService;
import com.kimcompany.jangbogbackendver2.Store.Dto.InsertEmployNotyDto;
import com.kimcompany.jangbogbackendver2.Store.Dto.SelectNotyDto;
import com.kimcompany.jangbogbackendver2.Store.StoreSelectService;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import com.kimcompany.jangbogbackendver2.Product.Dto.TryInsertDto;
import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import com.kimcompany.jangbogbackendver2.Product.Repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.cantFindProductCategory;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.cantFindStoreMessage;
import static com.kimcompany.jangbogbackendver2.Util.UtilService.getLoginUserId;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductSelectService productSelectService;
    private final ProductRepo productRepo;
    private final ProductEventService productEventService;
    private final ProductKindSelectService productKindSelectService;
    private final NotyService notyService;


    @Transactional(rollbackFor = Exception.class)
    public void save(TryInsertDto tryInsertDto){
        //가격 검증 다시 클라이언트는 숫자만 입력 서버에서 ,추가
        String price=UtilService.confirmPrice(tryInsertDto.getPrice());
        tryInsertDto.setPricePLusChar(price);
        long storeId = Long.parseLong(tryInsertDto.getId());
        confirmExist(storeId, tryInsertDto.getName());
        confirmCategory(Long.parseLong(tryInsertDto.getCategory()));
        ProductEntity productEntity = TryInsertDto.dtoToEntity(tryInsertDto);
        productRepo.save(productEntity);
        productEventService.save(productEntity.getId(), tryInsertDto.getEvents());
        notyService.doneInsert(tryInsertDto,UtilService.getPrincipalDetails().getMemberEntity());
    }
    private void confirmExist(long storeId,String productName){
        if(productSelectService.exist(productName,storeId)){
            throw new IllegalArgumentException("이미 등록 되어있는 상품입니다");
        }
    }
    private void confirmCategory(long categoryId){
        if(productKindSelectService.exist(categoryId)){
            throw new IllegalArgumentException(cantFindProductCategory);
        }
    }
    public Page<SelectListDto>selectForList(long storeId, SearchCondition searchCondition){
        return productSelectService.selectForList(storeId, searchCondition);
    }

}

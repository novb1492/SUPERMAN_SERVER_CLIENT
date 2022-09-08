package com.kimcompany.jangbogbackendver2.Product.Repo;

import com.kimcompany.jangbogbackendver2.Product.Dto.QSelectListDto;
import com.kimcompany.jangbogbackendver2.Product.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Product.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.ProductKind.Model.ProductCategoryEntity;
import com.kimcompany.jangbogbackendver2.ProductKind.Repo.ProductCategoryEntityRepo;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.deleteState;
import static com.kimcompany.jangbogbackendver2.Product.Model.QProductEntity.productEntity;

@RequiredArgsConstructor
public class ProductRepoImpl implements ProductRepoCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final ProductCategoryEntityRepo productCategoryEntityRepo;

    @Override
    public Boolean exist(long storeId, String productName) {
        Integer fetchFirst = jpaQueryFactory
                .selectOne()
                .from(productEntity)
                .where(productEntity.storeEntity.id.eq(storeId),productEntity.commonColumn.state.ne(deleteState),productEntity.name.eq(productName))
                .fetchFirst();
        return fetchFirst != null;
    }
    @Override
    public Page<SelectListDto>selectForList(int pageSize, long storeId, SearchCondition searchCondition){
        PageRequest pageRequest = PageRequest.of(searchCondition.getPage()-1, pageSize);
        long categoryId = Long.parseLong(searchCondition.getCategoryId());
        List<SelectListDto> selectListDtos= jpaQueryFactory
                .select(new QSelectListDto(productEntity))
                .from(productEntity)
                .where(productEntity.storeEntity.id.eq(storeId),productEntity.commonColumn.state.ne(deleteState), checkCondition(categoryId, searchCondition.getValue()))
                .orderBy(productEntity.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch ();
        // FetchCount
        JPAQuery<Long> count = jpaQueryFactory
                .select(productEntity.count())
                .from(productEntity)
                .where(productEntity.storeEntity.id.eq(storeId), productEntity.commonColumn.state.ne(deleteState), checkCondition(categoryId, searchCondition.getValue()));

        // Result
        Page<SelectListDto> selectListDtos2 = PageableExecutionUtils.getPage(selectListDtos, pageRequest, count::fetchOne);
        return selectListDtos2;
    }
    private BooleanExpression checkCondition(long categoryId, String val) {
        ProductCategoryEntity productCategoryEntity = productCategoryEntityRepo.findById(deleteState, categoryId).orElseGet(() -> null);
        if(productCategoryEntity!=null){
            if(!UtilService.confirmNull(val)){
                return productEntity.productKindEntity.id.eq(categoryId).and(productEntity.name.contains(val));
            }
            return productEntity.productKindEntity.id.eq(categoryId);
        }
        return null;
    }


}

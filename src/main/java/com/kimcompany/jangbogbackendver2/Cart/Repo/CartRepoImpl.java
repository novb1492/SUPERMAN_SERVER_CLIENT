package com.kimcompany.jangbogbackendver2.Cart.Repo;

import com.kimcompany.jangbogbackendver2.Cart.Dto.QSelectListDto;
import com.kimcompany.jangbogbackendver2.Cart.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Cart.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Cart.Model.QCartEntity;
import com.kimcompany.jangbogbackendver2.ProductEvent.Model.QProductEventEntity;
import com.kimcompany.jangbogbackendver2.ProductKind.Model.ProductCategoryEntity;
import com.kimcompany.jangbogbackendver2.ProductKind.Repo.ProductCategoryEntityRepo;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.kimcompany.jangbogbackendver2.Cart.Model.QCartEntity.cartEntity;
import static com.kimcompany.jangbogbackendver2.Product.Model.QProductEntity.productEntity;
import static com.kimcompany.jangbogbackendver2.ProductEvent.Model.QProductEventEntity.productEventEntity;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.deleteState;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.trueStateNum;

@RequiredArgsConstructor
public class CartRepoImpl implements CartRepoCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<SelectListDto>selectForList(SearchCondition searchCondition){
        PageRequest pageRequest = PageRequest.of(searchCondition.getPage()-1, searchCondition.getPageSize());
        List<SelectListDto> selectListDtos= jpaQueryFactory
                .select(new QSelectListDto(cartEntity,productEntity, productEventEntity))
                .from(cartEntity)
                .innerJoin(productEntity)
                .on(cartEntity.productEntity.id.eq(productEntity.id))
                .leftJoin(productEventEntity)
                .on(cartEntity.productEventEntities.id.eq(productEventEntity.id))
                .where(cartEntity.clientEntity.id.eq(searchCondition.getUserId()),cartEntity.commonColumn.state.eq(trueStateNum))
                .orderBy(cartEntity.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch ();
        // FetchCount
        JPAQuery<Long> count = jpaQueryFactory
                .select(cartEntity.count())
                .from(cartEntity)
                .where(cartEntity.clientEntity.id.eq(searchCondition.getUserId()),cartEntity.commonColumn.state.eq(trueStateNum));

        // Result
        Page<SelectListDto> selectListDtos2 = PageableExecutionUtils.getPage(selectListDtos, pageRequest, count::fetchOne);
        return selectListDtos2;
    }



}

package com.kimcompany.jangbogbackendver2.Order.Repo;

import com.kimcompany.jangbogbackendver2.Order.Dto.*;
import com.kimcompany.jangbogbackendver2.Payment.Model.QCardEntity;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.kimcompany.jangbogbackendver2.Member.Model.QClientEntity.clientEntity;
import static com.kimcompany.jangbogbackendver2.Order.Model.QOrderEntity.orderEntity;
import static com.kimcompany.jangbogbackendver2.Payment.Model.QCardEntity.cardEntity;
import static com.kimcompany.jangbogbackendver2.Product.Model.QProductEntity.productEntity;
import static com.kimcompany.jangbogbackendver2.ProductEvent.Model.QProductEventEntity.productEventEntity;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.*;

@RequiredArgsConstructor
public class OrderRepoImpl implements OrderRepoCustom{
    private final JPAQueryFactory jpaQueryFactory;

    public Page<SelectListDto> selectForList(SearchCondition searchCondition){
        PageRequest pageRequest = PageRequest.of(searchCondition.getPage()-1, orderListPageSize);
        List<SelectListDto> fetch = jpaQueryFactory.select(new QSelectListDto(orderEntity, clientEntity, cardEntity))
                .from(orderEntity)
                .leftJoin(clientEntity)
                .on(orderEntity.clientEntity.id.eq(clientEntity.id))
                .leftJoin(cardEntity)
                .on(cardEntity.id.eq(orderEntity.cardEntity.id))
                .fetchJoin()
                .where(whereState(searchCondition), whereDate(searchCondition), whereCategory(searchCondition),orderEntity.storeEntity.id.eq(searchCondition.getStoreId()))
                .orderBy(orderEntity.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .groupBy(orderEntity.cardEntity.id)
                .fetch();
        JPAQuery<Long> count = jpaQueryFactory
                .select(orderEntity.cardEntity.id.countDistinct())
                .from(orderEntity)
                .where(whereState(searchCondition), whereDate(searchCondition), whereCategory(searchCondition),orderEntity.storeEntity.id.eq(searchCondition.getStoreId()));
        // Result
        Page<SelectListDto> SelectListDtos = PageableExecutionUtils.getPage(fetch, pageRequest, count::fetchOne);
        return SelectListDtos;
    }
    private BooleanExpression whereState(SearchCondition searchCondition) {
        if (searchCondition.getState()== trueStateNum) {
            return orderEntity.commonColumn.state.eq(trueStateNum).or(orderEntity.commonColumn.state.eq(refundNum)).or(orderEntity.commonColumn.state.eq(deliveringState));
        }
        return orderEntity.commonColumn.state.eq(searchCondition.getState());
    }
    private BooleanExpression whereDate(SearchCondition searchCondition) {
        if (searchCondition.getPeriodFlag().equals("true")) {
            return orderEntity.commonColumn.created.between(Timestamp.valueOf(searchCondition.getStartDate()).toLocalDateTime()
                    , (Timestamp.valueOf(searchCondition.getEndDate()).toLocalDateTime()));
        }
        return null;
    }
    private BooleanExpression whereCategory( SearchCondition searchCondition) {
        String category = searchCondition.getCategory();
        if (category.equals("name")) {
            return orderEntity.clientEntity.fullName.contains(searchCondition.getKeyword());
        }else if(category.equals("addr")){
            return orderEntity.addressColumn.address.contains(searchCondition.getKeyword());
        }
        return null;
    }
    public List<SelectDto>selectByStoreIdAndCardId(long storeId,long cardId){
        return   jpaQueryFactory.select(new QSelectDto(orderEntity, productEntity, productEventEntity))
                .from(orderEntity)
                .leftJoin(productEntity)
                .on(productEntity.id.eq(orderEntity.productEntity.id))
                .leftJoin(productEventEntity)
                .on(productEventEntity.id.eq(orderEntity.productEventEntity.id))
                .fetchJoin()
                .where(orderEntity.commonColumn.state.ne(deleteState),orderEntity.cardEntity.id.eq(cardId),orderEntity.storeEntity.id.eq(storeId))
                .orderBy(orderEntity.id.desc())
                .fetch();
    }

    @Override
    public Optional<RefundDto> selectByOrderJoinCard(long orderId) {
        return Optional.ofNullable(jpaQueryFactory.select(new QRefundDto(orderEntity,cardEntity ))
                .from(orderEntity)
                .leftJoin(cardEntity)
                .on(orderEntity.cardEntity.id.eq(cardEntity.id))
                .fetchJoin()
                .where(orderEntity.commonColumn.state.ne(refundNum),orderEntity.commonColumn.state.ne(refundAllNum),orderEntity.id.eq(orderId))
                .fetchOne());
    }

}

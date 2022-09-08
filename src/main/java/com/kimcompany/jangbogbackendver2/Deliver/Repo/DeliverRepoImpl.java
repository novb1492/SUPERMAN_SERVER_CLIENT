package com.kimcompany.jangbogbackendver2.Deliver.Repo;

import com.kimcompany.jangbogbackendver2.Deliver.Dto.QSelectDto;
import com.kimcompany.jangbogbackendver2.Deliver.Dto.QSelectListDto;
import com.kimcompany.jangbogbackendver2.Deliver.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Deliver.Dto.SelectDto;
import com.kimcompany.jangbogbackendver2.Deliver.Dto.SelectListDto;

import com.kimcompany.jangbogbackendver2.Deliver.Model.DeliverDetailEntity;
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
import java.util.Optional;

import static com.kimcompany.jangbogbackendver2.Deliver.Model.QDeliverDetailEntity.deliverDetailEntity;
import static com.kimcompany.jangbogbackendver2.Deliver.Model.QDeliverEntity.deliverEntity;

import static com.kimcompany.jangbogbackendver2.Order.Model.QOrderEntity.orderEntity;
import static com.kimcompany.jangbogbackendver2.Payment.Model.QCardEntity.cardEntity;
import static com.kimcompany.jangbogbackendver2.Store.Model.QStoreEntity.storeEntity;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.*;

@RequiredArgsConstructor
public class DeliverRepoImpl implements DeliverRepoCustom {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<SelectListDto> selectForList(SearchCondition searchCondition) {
        System.out.println(searchCondition.toString());
        PageRequest pageRequest = PageRequest.of(searchCondition.getPage()-1, searchCondition.getPageSize());
        List<SelectListDto> fetch = jpaQueryFactory.select(new QSelectListDto(cardEntity, orderEntity, deliverEntity))
                .from(deliverDetailEntity)
                .leftJoin(deliverEntity)
                .on(deliverDetailEntity.deliverEntity.id.eq(deliverEntity.id))
                .leftJoin(cardEntity)
                .on(deliverDetailEntity.cardEntity.id.eq(cardEntity.id))
                .leftJoin(orderEntity)
                .on(orderEntity.cardEntity.id.eq(cardEntity.id))
                .where(whereState(searchCondition.getState()), deliverEntity.storeEntity.id.eq(searchCondition.getStoreId()))
                .groupBy(deliverDetailEntity.deliverEntity.id)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(deliverDetailEntity.id.desc())
                .fetch();

        JPAQuery<Long> count = jpaQueryFactory
                .select(deliverDetailEntity.deliverEntity.id.countDistinct())
                .from(deliverDetailEntity)
                .where(whereState(searchCondition.getState()),deliverEntity.storeEntity.id.eq(searchCondition.getStoreId()));
        return PageableExecutionUtils.getPage(fetch, pageRequest, count::fetchOne);
    }
    private BooleanExpression whereState(int state) {
        if (state==1) {
            return deliverEntity.commonColumn.state.eq(state).or(deliverEntity.commonColumn.state.eq(deliveringState));
        }
        return deliverEntity.commonColumn.state.eq(state).or(deliverEntity.commonColumn.state.eq(deliverCancelState));
    }
    @Override
    public List<SelectDto>selectForDetail(long storeId,long deliverId){
       return jpaQueryFactory.select(new QSelectDto(cardEntity, orderEntity,deliverDetailEntity))
                .from(deliverDetailEntity)
                .leftJoin(cardEntity)
                .on(deliverDetailEntity.cardEntity.id.eq(cardEntity.id))
                .leftJoin(orderEntity)
                .on(orderEntity.cardEntity.id.eq(cardEntity.id))
                .leftJoin(deliverEntity)
                .on(deliverDetailEntity.deliverEntity.id.eq(deliverEntity.id))
                .where(deliverDetailEntity.deliverEntity.id.eq(deliverId), deliverEntity.storeEntity.id.eq(storeId),deliverEntity.commonColumn.state.ne(deleteState))
                .groupBy(orderEntity.cardEntity.id)
                .fetch();
    }

    @Override
    public List<DeliverDetailEntity> selectByDeliverId(long deliverId) {
        return jpaQueryFactory
                .select(deliverDetailEntity)
                .from(deliverDetailEntity)
                .where(deliverDetailEntity.deliverEntity.id.eq(deliverId))
                .groupBy(deliverDetailEntity.cardEntity.id)
                .fetch();

    }


}

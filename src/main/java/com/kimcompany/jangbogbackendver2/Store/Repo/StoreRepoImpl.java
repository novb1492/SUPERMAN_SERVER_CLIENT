package com.kimcompany.jangbogbackendver2.Store.Repo;

import com.kimcompany.jangbogbackendver2.Employee.Model.QEmployeeEntity;
import com.kimcompany.jangbogbackendver2.Store.Dto.*;
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

import static com.kimcompany.jangbogbackendver2.Employee.Model.QEmployeeEntity.employeeEntity;
import static com.kimcompany.jangbogbackendver2.Store.Model.QStoreEntity.storeEntity;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.closingOfBusinessState;

@RequiredArgsConstructor
public class StoreRepoImpl implements StoreRepoCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Boolean exist(String address, String storeName) {
        Integer fetchFirst = jpaQueryFactory
                .selectOne()
                .from(storeEntity)
                .where(storeEntity.name.eq(storeName),storeEntity.addressColumn.address.eq(address),storeEntity.commonColumn.state.ne(closingOfBusinessState))
                .fetchFirst();

        return fetchFirst != null;
    }

    @Override
    public Boolean exist(long storeId, long adminId) {
        Integer fetchFirst = jpaQueryFactory
                .selectOne()
                .from(storeEntity)
                .where(storeEntity.id.eq(storeId),storeEntity.memberEntity.id.eq(adminId),storeEntity.commonColumn.state.ne(closingOfBusinessState))
                .fetchFirst();
        return fetchFirst != null;
    }

    @Override
    public Page<SelectRegiDto> selectForRegi(int page, long adminId,int PageSize) {
        PageRequest pageRequest = PageRequest.of(page-1, PageSize);
        List<SelectRegiDto> selectRegiDtos= jpaQueryFactory
                .select(new QSelectRegiDto(storeEntity))
                .from(storeEntity)
                .where(storeEntity.memberEntity.id.eq(adminId),storeEntity.commonColumn.state.ne(closingOfBusinessState))
                .orderBy(storeEntity.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch ();
        // FetchCount
        JPAQuery<Long> count = jpaQueryFactory
                .select(storeEntity.count())
                .from(storeEntity)
                .where(storeEntity.memberEntity.id.eq(adminId),storeEntity.commonColumn.state.ne(closingOfBusinessState));

        // Result
        Page<SelectRegiDto> SelectRegiDto = PageableExecutionUtils.getPage(selectRegiDtos, pageRequest, count::fetchOne);
        return SelectRegiDto;
    }

    @Override
    public Page<SelectListDto> selectForList( long adminId, int pageSize, SearchCondition searchCondition) {
        PageRequest pageRequest = PageRequest.of(searchCondition.getPage()-1, pageSize);
        List<SelectListDto> selectRegiDtos= jpaQueryFactory
                .select(new QSelectListDto(storeEntity))
                .from(storeEntity)
                .where(storeEntity.memberEntity.id.eq(adminId),storeEntity.commonColumn.state.ne(closingOfBusinessState),eqName(searchCondition.getCategory(),searchCondition.getKeyword()))
                .orderBy(storeEntity.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch ();
        // FetchCount
        JPAQuery<Long> count = jpaQueryFactory
                .select(storeEntity.count())
                .from(storeEntity)
                .where(storeEntity.memberEntity.id.eq(adminId),storeEntity.commonColumn.state.ne(closingOfBusinessState),eqName(searchCondition.getCategory(),searchCondition.getKeyword()));

        // Result
        Page<SelectListDto> SelectListDtos = PageableExecutionUtils.getPage(selectRegiDtos, pageRequest, count::fetchOne);
        return SelectListDtos;
    }

    @Override
    public Page<SelectListDto> selectForListOther(long adminId, int pageSize, SearchCondition searchCondition) {
        PageRequest pageRequest = PageRequest.of(searchCondition.getPage()-1, pageSize);
        List<SelectListDto> selectRegiDtos = jpaQueryFactory
                .select(new QSelectListDto(storeEntity))
                .from(storeEntity)
                .innerJoin(employeeEntity)
                .on(storeEntity.id.eq(employeeEntity.storeEntity.id))
                .where(employeeEntity.memberEntity.id.eq(adminId),storeEntity.commonColumn.state.ne(closingOfBusinessState),eqName(searchCondition.getCategory(),searchCondition.getKeyword()))
                .orderBy(storeEntity.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();
        // FetchCount
        JPAQuery<Long> count = jpaQueryFactory
                .select(storeEntity.count())
                .from(storeEntity)
                .innerJoin(employeeEntity)
                .on(storeEntity.id.eq(employeeEntity.storeEntity.id))
                .where(employeeEntity.memberEntity.id.eq(adminId),storeEntity.commonColumn.state.ne(closingOfBusinessState),eqName(searchCondition.getCategory(),searchCondition.getKeyword()));
        return PageableExecutionUtils.getPage(selectRegiDtos, pageRequest, count::fetchOne);
    }

    @Override
    public Page<SelectRegiDto> selectForRegiManage(int page, long adminId, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page-1, pageSize);
        List<SelectRegiDto> selectRegiDtos= jpaQueryFactory
                .select(new QSelectRegiDto(storeEntity))
                .from(storeEntity)
                .innerJoin(employeeEntity)
                .on(storeEntity.id.eq(employeeEntity.storeEntity.id))
                .where(employeeEntity.memberEntity.id.eq(adminId),storeEntity.commonColumn.state.ne(closingOfBusinessState))
                .orderBy(storeEntity.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch ();
        // FetchCount
        JPAQuery<Long> count = jpaQueryFactory
                .select(storeEntity.count())
                .from(storeEntity)
                .innerJoin(employeeEntity)
                .on(storeEntity.id.eq(employeeEntity.storeEntity.id))
                .where(employeeEntity.memberEntity.id.eq(adminId),storeEntity.commonColumn.state.ne(closingOfBusinessState));
        // Result
        Page<SelectRegiDto> SelectRegiDto = PageableExecutionUtils.getPage(selectRegiDtos, pageRequest, count::fetchOne);
        return SelectRegiDto;
    }

    private BooleanExpression eqName(String category, String val) {
        if (category.equals("addr")) {
            if(!UtilService.confirmNull(val)){
                return storeEntity.addressColumn.address.contains(val);
            }
        }else if(category.equals("name")){
            if(!UtilService.confirmNull(val)){
                return storeEntity.name.contains(val);
            }
        }
        return null;
    }
    public Optional<SelectInfo>selectByIdAndAdminId(long store,long adminId){
        SelectInfo selectInfo=jpaQueryFactory.select(new QSelectInfo(storeEntity))
                .from(storeEntity)
                .where(storeEntity.id.eq(store),storeEntity.memberEntity.id.eq(adminId),storeEntity.commonColumn.state.ne(closingOfBusinessState)).fetchOne();
        return Optional.ofNullable(selectInfo);
    }
    public Optional<SelectInfo>selectById(long storeId){
        SelectInfo selectInfo = jpaQueryFactory.select(new QSelectInfo(storeEntity))
                .from(storeEntity)
                .where(storeEntity.id.eq(storeId), storeEntity.commonColumn.state.ne(closingOfBusinessState)).fetchOne();
        return Optional.ofNullable(selectInfo);
    }
}

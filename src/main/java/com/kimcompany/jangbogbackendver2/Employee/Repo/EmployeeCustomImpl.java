package com.kimcompany.jangbogbackendver2.Employee.Repo;

import com.kimcompany.jangbogbackendver2.Employee.Dto.*;
import com.kimcompany.jangbogbackendver2.Employee.Model.QEmployeeEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.QMemberEntity;
import com.kimcompany.jangbogbackendver2.Store.Dto.InsertEmployNotyDto;
import com.kimcompany.jangbogbackendver2.Store.Dto.QInsertEmployNotyDto;
import com.kimcompany.jangbogbackendver2.Store.Model.QStoreEntity;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.kimcompany.jangbogbackendver2.Employee.Model.QEmployeeEntity.employeeEntity;
import static com.kimcompany.jangbogbackendver2.Member.Model.QMemberEntity.memberEntity;
import static com.kimcompany.jangbogbackendver2.Store.Model.QStoreEntity.storeEntity;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.*;

@RequiredArgsConstructor
public class EmployeeCustomImpl implements EmployeeCustom{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public boolean exist(long storeId, long userId,int state) {
        Integer fetchFirst = jpaQueryFactory
                .selectOne()
                .from(employeeEntity)
                .where(employeeEntity.storeEntity.id.eq(storeId),employeeEntity.memberEntity.id.eq(userId),employeeEntity.commonColumn.state.eq(state))
                .fetchFirst();
        return fetchFirst != null;
    }

    @Override
    public InsertEmployNotyDto selectStoreAndUser(long storeId, long userId) {
        return  jpaQueryFactory
                .select(new QInsertEmployNotyDto(memberEntity, storeEntity))
                .from(storeEntity)
                .leftJoin(memberEntity)
                .on(storeEntity.id.eq(storeId))
                .where(storeEntity.id.eq(storeId),memberEntity.id.eq(userId))
                .fetchFirst();
    }

    @Override
    public List<NotyEmployeeDto> selectEmployeeByStoreId(long storeId) {

        return jpaQueryFactory.select(new QNotyEmployeeDto(memberEntity.phone, memberEntity.email))
                .from(memberEntity)
                .innerJoin(employeeEntity)
                .on(employeeEntity.memberEntity.id.eq(memberEntity.id))
                .where(employeeEntity.commonColumn.state.eq(trueStateNum), employeeEntity.storeEntity.id.eq(storeId)
                        , memberEntity.role.eq(ROLE_ADMIN).or(memberEntity.role.eq(ROLE_MANAGE)))
                .fetch();
    }
    @Override
    public Page<SelectListDto>selectForList(SearchCondition searchCondition){
        PageRequest pageRequest = PageRequest.of(searchCondition.getPage()-1, searchCondition.getPageSize());
        List<SelectListDto> fetch = jpaQueryFactory.select(new QSelectListDto(memberEntity, employeeEntity))
                .from(employeeEntity)
                .innerJoin(memberEntity)
                .on(employeeEntity.memberEntity.id.eq(memberEntity.id))
                .where(employeeEntity.commonColumn.state.eq(trueStateNum), employeeEntity.storeEntity.id.eq(searchCondition.getStoreId()))
                .orderBy(employeeEntity.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();
        JPAQuery<Long> count = jpaQueryFactory
                .select(employeeEntity.count())
                .from(employeeEntity)
                .where(employeeEntity.commonColumn.state.eq(trueStateNum), employeeEntity.storeEntity.id.eq(searchCondition.getStoreId()));

        return PageableExecutionUtils.getPage(fetch, pageRequest, count::fetchOne);
    }

}

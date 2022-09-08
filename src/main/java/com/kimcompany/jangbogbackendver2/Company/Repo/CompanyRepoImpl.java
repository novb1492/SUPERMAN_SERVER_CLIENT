package com.kimcompany.jangbogbackendver2.Company.Repo;

import com.kimcompany.jangbogbackendver2.Company.Dto.QSelectListDetailDto;
import com.kimcompany.jangbogbackendver2.Company.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Company.Dto.SelectListDetailDto;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.kimcompany.jangbogbackendver2.Company.Model.QCompanyEntity.companyEntity;
import static com.kimcompany.jangbogbackendver2.Product.Model.QProductEntity.productEntity;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.deleteState;

@RequiredArgsConstructor
public class CompanyRepoImpl implements CompanyRepoCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<SelectListDetailDto>selectForList(int pageSize, long adminId, SearchCondition searchCondition){
        PageRequest pageRequest = PageRequest.of(searchCondition.getPage()-1, pageSize);
        List<SelectListDetailDto> selectListDtos= jpaQueryFactory
                .select(new QSelectListDetailDto(companyEntity))
                .from(companyEntity)
                .where(companyEntity.memberEntity.id.eq(adminId),companyEntity.commonColumn.state.ne(deleteState), checkCondition(searchCondition))
                .orderBy(companyEntity.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch ();
        // FetchCount
        JPAQuery<Long> count = jpaQueryFactory
                .select(companyEntity.count())
                .from(companyEntity)
                .where(companyEntity.memberEntity.id.eq(adminId),companyEntity.commonColumn.state.ne(deleteState), checkCondition(searchCondition));

        // Result
        Page<SelectListDetailDto> selectListDtos2 = PageableExecutionUtils.getPage(selectListDtos, pageRequest, count::fetchOne);
        return selectListDtos2;
    }
    private BooleanExpression checkCondition(SearchCondition searchConditionDto) {
        if(searchConditionDto.getCategory().equals("name")){
            if(!UtilService.confirmNull(searchConditionDto.getKeyword())){
                return companyEntity.name.contains(searchConditionDto.getKeyword());
            }
        }
        return null;
    }


}

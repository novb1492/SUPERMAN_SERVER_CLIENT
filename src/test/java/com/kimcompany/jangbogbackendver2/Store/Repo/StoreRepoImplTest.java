package com.kimcompany.jangbogbackendver2.Store.Repo;

import com.kimcompany.jangbogbackendver2.Employee.Model.QEmployeeEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.PrincipalDetails;
import com.kimcompany.jangbogbackendver2.Store.Dto.*;
import com.kimcompany.jangbogbackendver2.TestConfig;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

import static com.kimcompany.jangbogbackendver2.Employee.Model.QEmployeeEntity.employeeEntity;
import static com.kimcompany.jangbogbackendver2.Store.Model.QStoreEntity.storeEntity;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.closingOfBusinessState;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
@ActiveProfiles("test")
class StoreRepoImplTest {

    private Logger logger = LoggerFactory.getLogger(StoreRepoImplTest.class);

    @BeforeEach
    void setup() throws IllegalAccessException {
        PrincipalDetails principalDetails=new PrincipalDetails(MemberEntity.builder().id(1L).role("ADMIN").build());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(principalDetails, principalDetails.getPassword(), principalDetails.getAuthorities()));
    }
    @Autowired
    private TestEntityManager em;

    @Autowired
    private StoreRepo storeRepo;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;


    @Test
    @DisplayName("페이징 테스트")
    public void test(){
        Page<SelectRegiDto> selectRegiDtos = storeRepo.selectForRegi(1, 1,2);
        List<SelectRegiDto> selectRegiDtos1 = selectRegiDtos.getContent();
        for(SelectRegiDto s:selectRegiDtos1){
            logger.info("결과:{}",s.toString());
        }
    }
    @Test
    @DisplayName("페이지 초과 테스트")
    public void overPage(){
        Page<SelectRegiDto> selectRegiDtos = storeRepo.selectForRegi(10000, 1,1);
        Assertions.assertThat(selectRegiDtos.getContent()).isEmpty();
    }
    @Test
    @DisplayName("주소 검색 테스트")
    public void searchAddr(){
        SearchCondition searchCondition=get("ADMIN","addr","송",1);
        logger.info("결과:{}",search(searchCondition).getContent().size());
    }
    @Test
    @DisplayName("이름 검색 테스트")
    public void searchName(){
        SearchCondition searchCondition=get("ADMIN","name","한",1);
        logger.info("결과:{}",search(searchCondition).getContent().size());
    }
    @Test
    @DisplayName("ADMIN외 권한으로 조회")
    public void searchOther(){
        SearchCondition searchCondition=get("ADMIN",null,null,1);

        PageRequest pageRequest = PageRequest.of(0, 1000);
        List<SelectListDto> selectRegiDtos = jpaQueryFactory
                .select(new QSelectListDto(storeEntity))
                .from(storeEntity)
                .innerJoin(employeeEntity)
                .on(storeEntity.id.eq(employeeEntity.storeEntity.id))
                .where(employeeEntity.memberEntity.id.eq(3L),storeEntity.commonColumn.state.ne(closingOfBusinessState),eqName(searchCondition.getCategory(),searchCondition.getKeyword()))
                .orderBy(storeEntity.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        logger.info("admin외 권한으로 조회:{}",selectRegiDtos.get(0).toString());
//        // FetchCount
        JPAQuery<Long> count = jpaQueryFactory
                .select(storeEntity.count())
                .from(storeEntity)
                .innerJoin(employeeEntity)
                .on(storeEntity.id.eq(employeeEntity.storeEntity.id))
                .where(employeeEntity.memberEntity.id.eq(3L),storeEntity.commonColumn.state.ne(closingOfBusinessState),eqName(searchCondition.getCategory(),searchCondition.getKeyword()));

        // Result
        Page<SelectListDto> SelectListDtos = PageableExecutionUtils.getPage(selectRegiDtos, pageRequest, count::fetchOne);
        logger.info("admin외 권한으로 조회:{}",SelectListDtos.getContent().get(0).toString());
    }
    private Page<SelectListDto>search(SearchCondition searchCondition){
        PageRequest pageRequest = PageRequest.of(searchCondition.getPage()-1, 1000);
        List<SelectListDto> selectRegiDtos= jpaQueryFactory
                .select(new QSelectListDto(storeEntity))
                .from(storeEntity)
                .where(storeEntity.memberEntity.id.eq(1L),storeEntity.commonColumn.state.ne(closingOfBusinessState),eqName(searchCondition.getCategory(),searchCondition.getKeyword()))
                .orderBy(storeEntity.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch ();
        // FetchCount
        JPAQuery<Long> count = jpaQueryFactory
                .select(storeEntity.count())
                .from(storeEntity)
                .where(storeEntity.memberEntity.id.eq(1L),storeEntity.commonColumn.state.ne(closingOfBusinessState),eqName(searchCondition.getCategory(),searchCondition.getKeyword()));

        // Result
        Page<SelectListDto> SelectListDtos = PageableExecutionUtils.getPage(selectRegiDtos, pageRequest, count::fetchOne);
        return SelectListDtos;
    }
    private BooleanExpression eqName(String category,String val) {
        if (category.equals("addr")) {
            return storeEntity.addressColumn.address.contains(val);
        }
        return storeEntity.name.contains(val);
    }
    private SearchCondition get(String role,String category,String keyword,int page){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setCategory(category);
        searchCondition.setPage(page);
        searchCondition.setKeyword(keyword);
        return searchCondition;
    }
}
package com.kimcompany.jangbogbackendver2.Product.Repo;

import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.PrincipalDetails;
import com.kimcompany.jangbogbackendver2.Product.Dto.QSelectListDto;
import com.kimcompany.jangbogbackendver2.Product.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Product.Model.QProductEntity;
import com.kimcompany.jangbogbackendver2.ProductKind.Model.ProductCategoryEntity;
import com.kimcompany.jangbogbackendver2.ProductKind.Repo.ProductCategoryEntityRepo;
import com.kimcompany.jangbogbackendver2.Store.Dto.QSelectRegiDto;
import com.kimcompany.jangbogbackendver2.Store.Dto.SelectRegiDto;
import com.kimcompany.jangbogbackendver2.Store.Repo.StoreRepo;
import com.kimcompany.jangbogbackendver2.TestConfig;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

import java.util.List;

import static com.kimcompany.jangbogbackendver2.Product.Model.QProductEntity.productEntity;
import static com.kimcompany.jangbogbackendver2.Store.Model.QStoreEntity.storeEntity;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
@ActiveProfiles("test")
class ProductRepoImplTest {
    private Logger logger = LoggerFactory.getLogger(ProductRepoImplTest.class);
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

    @Autowired
    private ProductCategoryEntityRepo productCategoryEntityRepo;

    @Test
    @DisplayName("상품조회 기본 페이징 테스트")
    void test(){
        Page<SelectListDto> select = select(1, 3, 1,null,null);
        List<SelectListDto> contents = select.getContent();
        for(SelectListDto content:contents){
            logger.info("결과:{}", content.getName());
        }
    }
    @Test
    @DisplayName("상품조회 기본 조건 페이징 테스트")
    void test2(){
        Page<SelectListDto> select = select(1, 3, 1,10L,null);
        List<SelectListDto> contents = select.getContent();
        for(SelectListDto content:contents){
            logger.info("결과:{}", content.getName());
        }
    }
    @Test
    @DisplayName("상품조회 기본 조건 페이징 테스트 검색어 추가")
    void test3(){
        Page<SelectListDto> select = select(1, 3, 1,10L,"ㄱ");
        List<SelectListDto> contents = select.getContent();
        for(SelectListDto content:contents){
            logger.info("결과:{}", content.getName());
        }
    }
    Page<SelectListDto>select(int page,int pageSize,long storeId,Long categoryId,String val){
        PageRequest pageRequest = PageRequest.of(page-1, pageSize);
        List<SelectListDto> selectListDtos= jpaQueryFactory
                .select(new QSelectListDto(productEntity))
                .from(productEntity)
                .where(productEntity.storeEntity.id.eq(storeId),productEntity.commonColumn.state.ne(deleteState), checkCondition(categoryId, val))
                .orderBy(productEntity.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch ();
        // FetchCount
        JPAQuery<Long> count = jpaQueryFactory
                .select(productEntity.count())
                .from(productEntity)
                .where(productEntity.storeEntity.id.eq(storeId), productEntity.commonColumn.state.ne(deleteState), checkCondition(categoryId, val));

        // Result
        Page<SelectListDto> selectListDtos2 = PageableExecutionUtils.getPage(selectListDtos, pageRequest, count::fetchOne);
        return selectListDtos2;
    }
    private BooleanExpression checkCondition(long categoryId,String val) {
        ProductCategoryEntity productCategoryEntity = productCategoryEntityRepo.findById(deleteState, categoryId).orElseGet(() -> null);
        if(productCategoryEntity!=null){
            if(StringUtils.hasText(val)){
                return productEntity.productKindEntity.id.eq(categoryId).and(productEntity.name.contains(val));
            }
            return productEntity.productKindEntity.id.eq(categoryId);
        }
        return null;
    }


}
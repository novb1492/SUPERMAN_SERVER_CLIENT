package com.kimcompany.jangbogbackendver2.Cart.Repo;

import com.kimcompany.jangbogbackendver2.Cart.Dto.QSelectListDto;
import com.kimcompany.jangbogbackendver2.Cart.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Cart.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.PrincipalDetails;
import com.kimcompany.jangbogbackendver2.TestConfig;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.kimcompany.jangbogbackendver2.Cart.Model.QCartEntity.cartEntity;
import static com.kimcompany.jangbogbackendver2.Product.Model.QProductEntity.productEntity;
import static com.kimcompany.jangbogbackendver2.ProductEvent.Model.QProductEventEntity.productEventEntity;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.trueStateNum;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
@ActiveProfiles("test")
class CartRepoImplTest {
    private final Logger logger= LoggerFactory.getLogger(CartRepoImplTest.class);
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    void setup() throws IllegalAccessException {
        PrincipalDetails principalDetails=new PrincipalDetails(MemberEntity.builder().id(1L).role("ADMIN").build());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(principalDetails, principalDetails.getPassword(), principalDetails.getAuthorities()));
    }

    @Test
    public void test(){
        SearchCondition searchCondition=new SearchCondition(1);
        PageRequest pageRequest = PageRequest.of(searchCondition.getPage()-1, searchCondition.getPageSize());
        List<SelectListDto> selectListDtos= jpaQueryFactory
                .select(new QSelectListDto(cartEntity,productEntity, productEventEntity))
                .from(cartEntity)
                .innerJoin(productEntity)
                .on(cartEntity.productEntity.id.eq(productEntity.id))
                .leftJoin(productEventEntity)
                .on(cartEntity.productEventEntities.id.eq(productEventEntity.id))
                .where(cartEntity.clientEntity.id.eq(searchCondition.getUserId()),cartEntity.commonColumn.state.eq(trueStateNum))
                .orderBy(productEntity.id.desc())
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
        logger.info("result:",selectListDtos2.toString());
    }
}
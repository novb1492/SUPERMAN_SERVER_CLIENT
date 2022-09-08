package com.kimcompany.jangbogbackendver2.Employee.Repo;

import com.kimcompany.jangbogbackendver2.Employee.Dto.NotyEmployeeDto;
import com.kimcompany.jangbogbackendver2.Employee.Dto.QNotyEmployeeDto;
import com.kimcompany.jangbogbackendver2.Employee.Dto.QSelectListDto;
import com.kimcompany.jangbogbackendver2.Employee.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Member.Model.QMemberEntity;
import com.kimcompany.jangbogbackendver2.Store.Dto.InsertEmployNotyDto;
import com.kimcompany.jangbogbackendver2.Store.Repo.StoreRepo;
import com.kimcompany.jangbogbackendver2.TestConfig;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

import java.util.List;

import static com.kimcompany.jangbogbackendver2.Deliver.Model.QDeliverDetailEntity.deliverDetailEntity;
import static com.kimcompany.jangbogbackendver2.Deliver.Model.QDeliverEntity.deliverEntity;
import static com.kimcompany.jangbogbackendver2.Employee.Model.QEmployeeEntity.employeeEntity;
import static com.kimcompany.jangbogbackendver2.Member.Model.QMemberEntity.memberEntity;
import static com.kimcompany.jangbogbackendver2.Text.BasicText.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
class EmployeeCustomImplTest {

    private Logger logger = LoggerFactory.getLogger(EmployeeCustomImplTest.class);

    @Autowired
    private TestEntityManager em;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Test
    @DisplayName("조인 테스트")
    void test(){
        InsertEmployNotyDto insertEmployNotyDto = employeeRepo.selectStoreAndUser(1, 3);
        logger.info("결과값:{}",insertEmployNotyDto.toString());
    }

    @Test
    @DisplayName("소속매장 매니저 어드민 전화/이메일 꺼내기")
    void test2(){
        List<NotyEmployeeDto> fetch = jpaQueryFactory.select(new QNotyEmployeeDto(memberEntity.phone, memberEntity.email))
                .from(memberEntity)
                .innerJoin(employeeEntity)
                .on(employeeEntity.memberEntity.id.eq(memberEntity.id))
                .where(employeeEntity.commonColumn.state.eq(trueStateNum), employeeEntity.storeEntity.id.eq(1L)
                        , memberEntity.role.eq(ROLE_ADMIN).or(memberEntity.role.eq(ROLE_MANAGE)))
                .fetch();
        for(NotyEmployeeDto dto:fetch){
            logger.info("결과:{}",dto.getEmail());
        }
    }
    @Test
    @DisplayName("소속 직원들 조회 쿼리")
    void test3(){
        PageRequest pageRequest = PageRequest.of(0, 1);
        List<SelectListDto> fetch = jpaQueryFactory.select(new QSelectListDto(memberEntity, employeeEntity))
                .from(employeeEntity)
                .leftJoin(memberEntity)
                .on(employeeEntity.memberEntity.id.eq(memberEntity.id))
                .where(employeeEntity.commonColumn.state.eq(trueStateNum), employeeEntity.storeEntity.id.eq(1L))
                .orderBy(employeeEntity.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();
        JPAQuery<Long> count = jpaQueryFactory
                .select(employeeEntity.count())
                .from(employeeEntity)
                .where(employeeEntity.commonColumn.state.eq(trueStateNum), employeeEntity.storeEntity.id.eq(1L));

        Page<SelectListDto> page = PageableExecutionUtils.getPage(fetch, pageRequest, count::fetchOne);


    }
}
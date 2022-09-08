package com.kimcompany.jangbogbackendver2.Employee;

import com.kimcompany.jangbogbackendver2.Employee.Dto.TryInsertDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeServiceTest {
    private Logger logger = LoggerFactory.getLogger(EmployeeServiceTest.class);

    @Autowired
    private EmployeeService employeeService;

    @Test
    @DisplayName("정상적 등록 테스트")
    @WithUserDetails("kim")
    @Transactional
    public void suc(){
        TryInsertDto tryInsertDto = set("1", "2");
        employeeService.save(tryInsertDto);
    }
    @Test
    @DisplayName("직원등록 실패 테스트")
    @WithUserDetails("kim")
    public void fail(){
        TryInsertDto tryInsertDto = set("!", "2");
        assertThrows(IllegalArgumentException.class, () -> employeeService.save(tryInsertDto));
        logger.info("매장등록 400 error 테스트통과");
        TryInsertDto tryInsertDto2 = set("1", "3");
        assertThrows(IllegalArgumentException.class, () -> employeeService.save(tryInsertDto2));
        logger.info("매장등록 이미 있는 직원 등록 요청 테스트통과");
    }
    @Test
    @DisplayName("직원등록 실패 테스트 소유매장아님")
    @WithUserDetails("kim")
    public void failToNotHave(){
        TryInsertDto tryInsertDto = set("100", "3");
        employeeService.save(tryInsertDto);
    }
    private TryInsertDto set(String storeId,String userId){
        TryInsertDto tryInsertDto = new TryInsertDto();
        tryInsertDto.setStoreId(storeId);
        tryInsertDto.setUserId(userId);
        return tryInsertDto;
    }
}
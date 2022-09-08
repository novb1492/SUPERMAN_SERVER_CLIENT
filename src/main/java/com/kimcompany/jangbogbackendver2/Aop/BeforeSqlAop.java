package com.kimcompany.jangbogbackendver2.Aop;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.kimcompany.jangbogbackendver2.Deliver.Dto.ChangeDetailDto;
import com.kimcompany.jangbogbackendver2.Deliver.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Deliver.Dto.StartDeliverDto;
import com.kimcompany.jangbogbackendver2.Employee.Dto.TryInsertDto;
import com.kimcompany.jangbogbackendver2.Employee.EmployeeSelectService;
import com.kimcompany.jangbogbackendver2.Exception.Exceptions.TokenException;
import com.kimcompany.jangbogbackendver2.Filter.AuthorizationFilter;
import com.kimcompany.jangbogbackendver2.Product.Service.ProductSelectService;
import com.kimcompany.jangbogbackendver2.Store.StoreSelectService;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Util.AuthorizationService;
import com.kimcompany.jangbogbackendver2.Util.EtcService;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Map;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.*;
import static com.kimcompany.jangbogbackendver2.Util.UtilService.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * 해당 매장의 접근 권리가 있는지 확인하는 aop입니다
 */
@Component
@RequiredArgsConstructor
@Aspect
@Slf4j
public class BeforeSqlAop {
    private final EtcService etcService;
    private final AuthorizationService authorizationService;
    /**
     * 상품/직원등록전 해당 매장에 대한
     * 권리가 있는지 확인
     * @param joinPoint
     * @throws Throwable
     */
    @Before("execution(* com.kimcompany.jangbogbackendver2.Employee.EmployeeService.save(..))"
            +"||execution(* com.kimcompany.jangbogbackendver2.Product.Service.ProductService.save(..))"
            +"||execution(* com.kimcompany.jangbogbackendver2.Deliver.Service.DeliverService.save(..))")
    public void beforeSaveCheck(JoinPoint joinPoint) throws Throwable {
        log.info("save전 소유 검사");
        Object[] values=joinPoint.getArgs();
        long storeId = 0;
        for(Object dto:values){
            if(dto instanceof TryInsertDto){
                log.info("종업원 등록전 검사");
                TryInsertDto tryInsertDto=(TryInsertDto)dto;
                storeId = Long.parseLong(tryInsertDto.getStoreId());
                break;
            }else if(dto instanceof com.kimcompany.jangbogbackendver2.Product.Dto.TryInsertDto){
                log.info("상품 등록전 검사");
                com.kimcompany.jangbogbackendver2.Product.Dto.TryInsertDto tryInsertDto=(com.kimcompany.jangbogbackendver2.Product.Dto.TryInsertDto)dto;
                storeId = Long.parseLong(tryInsertDto.getId());
                break;
            }else if(dto instanceof com.kimcompany.jangbogbackendver2.Deliver.Dto.TryInsertDto ){
                log.info("배달방 등록전 검사");
                com.kimcompany.jangbogbackendver2.Deliver.Dto.TryInsertDto tryInsertDto = (com.kimcompany.jangbogbackendver2.Deliver.Dto.TryInsertDto) dto;
                storeId =Long.parseLong(tryInsertDto.getStoreId());
                break;
            }
        }
        etcService.confirmOwn(storeId);
    }

    /**
     * 해당 매장의 주문/직원/상품/배달/매출 조회전
     * 권한이있는지 확인
     * @param joinPoint
     * @throws Throwable
     */
    @Before("execution(* com.kimcompany.jangbogbackendver2.Product.Service.ProductService.selectForList(..))"
            +"||execution(* com.kimcompany.jangbogbackendver2.Deliver.Service.DeliverSelectService.selectForList(..))"
            +"||execution(* com.kimcompany.jangbogbackendver2.Employee.EmployeeSelectService.selectForList(..))"
    )
    public void checkBelong(JoinPoint joinPoint) throws Throwable{
        log.info("select전 소유 검사");
        long storeId = 0;
        for (Object obj : joinPoint.getArgs()) {
            if (obj instanceof Long) {
                storeId =  (long) obj;
                break;
            }else if(obj instanceof SearchCondition){
                log.info("배달리스트 조회전 소유검사");
                storeId = ((SearchCondition) obj).getStoreId();
                break;
            }else if(obj instanceof com.kimcompany.jangbogbackendver2.Employee.Dto.SearchCondition){
                storeId = ((com.kimcompany.jangbogbackendver2.Employee.Dto.SearchCondition) obj).getStoreId();
                break;
            }
        }
        etcService.confirmOwn(storeId);
    }

//    /**
//     * 웹소켓 연결전 인증주입 인증 방식이 쿠키로 버뀌어서
    // 웹소캣 내부에서 검사
//     * @param joinPoint
//     * @throws Throwable
//     */
//    @Before("execution(* com.kimcompany.jangbogbackendver2.Deliver.Service.DeliverPositionHandler.afterConnectionEstablished(..))")
//    public void ws(JoinPoint joinPoint) throws Throwable{
//        log.info("웹소캣 접근전 소유 검사");
//        WebSocketSession session = null;
//        for (Object obj : joinPoint.getArgs()) {
//            if (obj instanceof WebSocketSession) {
//                session =  (WebSocketSession) obj;
//                break;
//            }
//        }
//        String query = session.getUri().getQuery();
//        Map<String, Object> queryMap = getQueryMap(query);
//        /**
//         * 테스트위해 유저는 제외
//         */
//        if(!queryMap.get("role").equals(ROLE_USER)){
//            System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
////            etcService.confirmOwn(Long.parseLong(queryMap.get("storeId").toString()));
//        }
//    }

    /**
     * 업데이트전 소유확인
     * @param joinPoint
     * @throws Throwable
     */
    @Before("execution(* com.kimcompany.jangbogbackendver2.Deliver.Service.DeliverService.updateDeliverAndDeliverDetailAndOrderState(..))"
            +"||execution(* com.kimcompany.jangbogbackendver2.Deliver.Service.DeliverService.updateDeliverAndDeliverDetailAndOrderState(..))"
    )
    public void checkBeforeUpdate(JoinPoint joinPoint) throws Throwable{
        log.info("update전 소유 검사");
        long storeId = 0;
        for (Object obj : joinPoint.getArgs()) {
            if (obj instanceof StartDeliverDto) {
                storeId = ((StartDeliverDto) obj).getStoreId();
                break;
            }else if(obj instanceof ChangeDetailDto){
                storeId = ((ChangeDetailDto) obj).getStoreId();
                break;
            }
        }
        etcService.confirmOwn(storeId);
    }


}

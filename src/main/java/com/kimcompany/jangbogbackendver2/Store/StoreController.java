package com.kimcompany.jangbogbackendver2.Store;

import com.kimcompany.jangbogbackendver2.Store.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Store.Dto.TryInsertDto;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.kimcompany.jangbogbackendver2.Util.UtilService.confirmNull;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StoreController {
    private final StoreService  storeService;

    /**
     * 매장등록
     * @param tryInsertDto
     * @return
     */
    @RequestMapping(value = "/admin/store/save",method = POST)
    public ResponseEntity<?> regiStore(@Valid @RequestBody TryInsertDto tryInsertDto) throws ParseException {
        storeService.save(tryInsertDto);
        JSONObject response = new JSONObject();
        response.put("message","매장등록이 완료 되었습니다");
        return ResponseEntity.ok().body(response);
    }

    /**
     * 직원등록을 위한 매장조회
     * @param request
     * @return
     */
    @RequestMapping(value = "/manage/store/regi/list",method = GET)
    public ResponseEntity<?>selectList(HttpServletRequest request){
        int page = Integer.parseInt(request.getParameter("page"));
        return ResponseEntity.ok().body(storeService.selectForRegi(page));
    }

    /**
     * 매장 조회
     * @param request
     * @return
     */
    @RequestMapping(value = "/store/list",method = GET)
    public ResponseEntity<?>selectListForStore(HttpServletRequest request){
        int page = Integer.parseInt(request.getParameter("page"));
        String category=request.getParameter("category");
        String keyword=request.getParameter("keyword");
        return ResponseEntity.ok().body(storeService.selectForList(SearchCondition.set(page,keyword,category)));
    }

    /**
     * 매장 상세조회
     * @param id
     * @return
     */
    @RequestMapping(value = "/store/{id}",method = GET)
    public ResponseEntity<?>selectStoreInfo(@PathVariable String id){
        return ResponseEntity.ok().body(storeService.selectStoreInfo(Long.parseLong(id)));
    }

}

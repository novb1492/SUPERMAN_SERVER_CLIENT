package com.kimcompany.jangbogbackendver2.Employee;


import com.kimcompany.jangbogbackendver2.Employee.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Employee.Dto.TryInsertDto;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.employeePageSize;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeSelectService employeeSelectService;
    /**
     * 직원등록
     * @param tryInsertDto
     * @return
     */
    @RequestMapping(value = "/manage/employee/save",method = POST)
    public ResponseEntity<?>save(@Valid @RequestBody TryInsertDto tryInsertDto){
        employeeService.save(tryInsertDto);
        JSONObject response = new JSONObject();
        response.put("message","종업원 등록에 성공했습니다");
        return ResponseEntity.ok().body(response);
    }

    /**
     * 매장별 직원조회
     * @param request
     * @param storeId
     * @return
     */
    @RequestMapping(value = "/employee/list/{storeId}",method = GET)
    public ResponseEntity<?>selectForList(HttpServletRequest request, @PathVariable String storeId){
        int page = Integer.parseInt(request.getParameter("page"));
        long storeIdToLong = Long.parseLong(storeId);
        SearchCondition searchCondition = SearchCondition.set(page, employeePageSize, storeIdToLong);
        return ResponseEntity.ok().body(employeeSelectService.selectForList(searchCondition));
    }

}

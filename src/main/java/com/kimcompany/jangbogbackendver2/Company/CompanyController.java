package com.kimcompany.jangbogbackendver2.Company;

import com.kimcompany.jangbogbackendver2.Company.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Company.Dto.TryInsertDto;
import com.kimcompany.jangbogbackendver2.Company.Service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    /**
     * 사업자번호 등록
     */
    @RequestMapping(value = "/admin/company/save",method = RequestMethod.POST)
    public ResponseEntity<?>save(@Valid @RequestBody TryInsertDto tryInsertDto) throws ParseException {
        companyService.save(tryInsertDto);
        JSONObject response=new JSONObject();
        response.put("message","등록성공");
        return ResponseEntity.ok().body(response);
    }
    /**
     * 사업자번호 조회
     */
    @RequestMapping(value = "/admin/company/list/all",method = RequestMethod.GET)
    public ResponseEntity<?>selectAll() {
        return ResponseEntity.ok().body(companyService.selectForListNotPaging());
    }
    /**
     * 사업자 번호 조회 페이징
     */
    @RequestMapping(value = "/admin/company/list",method = RequestMethod.GET)
    public ResponseEntity<?>selectAll(HttpServletRequest request) {
        SearchCondition searchCondition = SearchCondition.set(Integer.parseInt(request.getParameter("page"))
                , request.getParameter("keyword")
                , request.getParameter("category"));
        return ResponseEntity.ok().body(companyService.selectForList(searchCondition));
    }
}

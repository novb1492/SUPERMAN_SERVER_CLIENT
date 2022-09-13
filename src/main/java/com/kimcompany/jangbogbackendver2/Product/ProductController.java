package com.kimcompany.jangbogbackendver2.Product;

import com.kimcompany.jangbogbackendver2.Product.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Product.Dto.TryInsertDto;
import com.kimcompany.jangbogbackendver2.Product.Service.ProductSelectService;
import com.kimcompany.jangbogbackendver2.Product.Service.ProductService;
import com.kimcompany.jangbogbackendver2.ProductKind.Service.ProductKindSelectService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductSelectService productSelectService;

    /**
     * 매장별 제품조회
     * @param an
     * @param pn
     * @param request
     * @return
     */
    @RequestMapping(value = "/product/list/{an}/{pn}",method = RequestMethod.GET)
    public ResponseEntity<?>selectList(@PathVariable String an, @PathVariable String pn,HttpServletRequest request){
        return ResponseEntity.ok().body(productSelectService.selectForList(new SearchCondition(an,pn,request)));
    }

    /**
     * 제품조회 api
     * @param id
     * @return
     */
    @RequestMapping(value = "/product/{id}",method = RequestMethod.GET)
    public ResponseEntity<?>selectById(@PathVariable String id){
        return ResponseEntity.ok().body(productSelectService.selectForId(Long.parseLong(id)));
    }
}

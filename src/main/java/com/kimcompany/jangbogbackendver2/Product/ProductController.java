package com.kimcompany.jangbogbackendver2.Product;

import com.kimcompany.jangbogbackendver2.Product.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Product.Dto.TryInsertDto;
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
    private final ProductService productService;

    /**
     * 상품등록
     * @param tryInsertDto
     * @return
     */
    @RequestMapping(value = "/manage/product/save",method = RequestMethod.POST)
    public ResponseEntity<?>save(@Valid @RequestBody TryInsertDto tryInsertDto){
        productService.save(tryInsertDto);
        JSONObject response = new JSONObject();
        response.put("message", "상품 저장완료");
        return ResponseEntity.ok().body(response);
    }

    /**
     * 상품리스트조회
     * @param storeId
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/product/list/{storeId}",method = RequestMethod.GET)
    public ResponseEntity<?>selectForList(@PathVariable String storeId, HttpServletRequest request){
        SearchCondition searchCondition = new SearchCondition(Integer.parseInt(request.getParameter("page").toString())
                , Optional.ofNullable(request.getParameter("category")).orElseGet(()->null)
                , Optional.ofNullable(request.getParameter("val")).orElseGet(()->null));
        return ResponseEntity.ok().body(productService.selectForList(Long.parseLong(storeId), searchCondition));
    }
}

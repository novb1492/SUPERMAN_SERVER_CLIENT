package com.kimcompany.jangbogbackendver2.Cart;

import com.kimcompany.jangbogbackendver2.Cart.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Cart.Dto.TryInsertDto;
import com.kimcompany.jangbogbackendver2.Cart.Dto.tryDeleteDto;
import com.kimcompany.jangbogbackendver2.Cart.Service.CartSelectService;
import com.kimcompany.jangbogbackendver2.Cart.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class CartController {
    private final CartService cartService;
    private final CartSelectService cartSelectService;

    /**
     * 장바구니 담는 요청
     * @param tryInsertDto
     * @return
     */
    @RequestMapping(value = "/cart/save",method = RequestMethod.POST)
    public ResponseEntity<?>save(@Valid @RequestBody TryInsertDto tryInsertDto){
        cartService.save(tryInsertDto);
        JSONObject response = new JSONObject();
        response.put("message","장바구니에 담았습니다");
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/cart/list",method = RequestMethod.GET)
    public ResponseEntity<?>save(HttpServletRequest request){
        return ResponseEntity.ok(cartSelectService.selectForList(new SearchCondition(Integer.parseInt(request.getParameter("page")))));
    }

    @RequestMapping(value = "/cart",method = RequestMethod.DELETE)
    public ResponseEntity<?>deleteCart(@Valid @RequestBody tryDeleteDto tryDeleteDto){
        System.out.println(tryDeleteDto.toString());
        cartService.deleteById(tryDeleteDto);
        JSONObject response=new JSONObject();
        response.put("message", "장바구니에서 제거되었습니다");
        return ResponseEntity.ok(response);
    }
}

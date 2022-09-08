package com.kimcompany.jangbogbackendver2.Order;

import com.kimcompany.jangbogbackendver2.Order.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Order.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @RequestMapping(value = "/order/list/{storeId}/{state}",method = RequestMethod.GET)
    public ResponseEntity<?>selectForList(HttpServletRequest request, @PathVariable String storeId, @PathVariable String state){
        SearchCondition searchCondition = SearchCondition.set(Long.parseLong(storeId)
                , request.getParameter("category")
                , request.getParameter("keyword")
                , Integer.parseInt(state)
                , request.getParameter("periodFlag")
                , request.getParameter("startDate")
                , request.getParameter("endDate")
                , Integer.parseInt(request.getParameter("page")));
        return ResponseEntity.ok().body(orderService.selectForList(searchCondition));
    }
    @RequestMapping(value = "/order/{storeId}/{cardId}",method = RequestMethod.GET)
    public ResponseEntity<?>selectForList( @PathVariable String storeId, @PathVariable String cardId){
        return ResponseEntity.ok().body(orderService.selectForDetail(Long.parseLong(storeId),Long.parseLong(cardId)));

    }

}

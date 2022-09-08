package com.kimcompany.jangbogbackendver2.Order.Dto;

import com.kimcompany.jangbogbackendver2.Order.Model.OrderEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class TryRefundDto {

    @NotBlank(message = "환불할 상품을 선택해 주세요")
    private String orderId;

    @Min(value = 1,message = "환불할 상품의 최소 개수는 1개입니다")
    private int count;

    public static List<TryRefundDto>setForAllRefund(List<OrderEntity>orderEntities){
        List<TryRefundDto> tryRefundDtos = new ArrayList<>();
        for (OrderEntity o:orderEntities){
            TryRefundDto tryRefundDto = new TryRefundDto();
            tryRefundDto.setCount(o.getTotalCount());
            tryRefundDto.setOrderId(Long.toString(o.getId()));
            tryRefundDtos.add(tryRefundDto);
        }
        return tryRefundDtos;
    }
}

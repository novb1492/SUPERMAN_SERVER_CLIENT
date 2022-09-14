package com.kimcompany.jangbogbackendver2.Cart.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class TryUpdateCountDto {

    @NotNull(message = "장바구니 번호가 유실되었습니다")
    private Long id;

    @Min(value = 1,message = "최소 주문가능 금액은 1개입니다")
    private int count;
}

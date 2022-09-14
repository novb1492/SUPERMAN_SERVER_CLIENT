package com.kimcompany.jangbogbackendver2.Cart.Dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@NoArgsConstructor
@Data
public class TryInsertDto {

    @Min(value = 1,message = "구매가능 최소수량은 1개입니다")
    private int count;

    @NotNull(message = "상품번호가 유실 되었습니다")
    private Long productId;

    private Long eventId;

}

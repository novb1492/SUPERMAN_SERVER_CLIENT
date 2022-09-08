package com.kimcompany.jangbogbackendver2.Deliver.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class StartDeliverDto {

    @NotNull(message = "배달번호가 유실 되었습니다")
    private Long deliverId;
    @NotNull(message = "매장번호가 유실되었습니다")
    private Long storeId;

    private int state;

}

package com.kimcompany.jangbogbackendver2.Deliver.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class ChangeDetailDto {

    @NotNull(message = "배달번호상세번호가 유실 되었습니다")
    private Long deliverDetailId;
    @NotNull(message = "배달방번호가 유실 되었습니다")
    private Long deliverId;
    @NotNull(message = "매장번호가 유실되었습니다")
    private Long storeId;
    @NotNull(message = "상태값이 유실되었습니다")
    private Integer state;
}

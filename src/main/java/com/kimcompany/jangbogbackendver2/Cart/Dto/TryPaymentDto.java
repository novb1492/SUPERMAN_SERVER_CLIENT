package com.kimcompany.jangbogbackendver2.Cart.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Data
public class TryPaymentDto {

    @Size(min = 1,message = "주문할 상품을 선택해주세요")
    List<Map<String,Object>>payments;

    @NotBlank(message = "주소를 입력해주세요")
    private String address;
    @NotBlank(message = "주소를 입력해주세요")
    private String detailAddress;
    @NotBlank(message = "주소를 입력해주세요")
    private String postCode;
}

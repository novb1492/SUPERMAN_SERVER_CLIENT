package com.kimcompany.jangbogbackendver2.Cart.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@Data
public class tryDeleteDto {

    @Size(min = 1,message = "최소 1개 이상 선택해주세요")
    private List<Long>ids;
}

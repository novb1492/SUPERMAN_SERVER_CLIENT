package com.kimcompany.jangbogbackendver2.Order.Dto;

import com.kimcompany.jangbogbackendver2.Payment.Dto.SelectForOrderDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class SelectDtoAndCard {
    List<SelectDto>selectDtos;
    SelectForOrderDto selectForOrderDto;

    public SelectDtoAndCard(List<SelectDto> selectDtos, SelectForOrderDto selectForOrderDto) {
        this.selectDtos = selectDtos;
        this.selectForOrderDto = selectForOrderDto;
    }
}

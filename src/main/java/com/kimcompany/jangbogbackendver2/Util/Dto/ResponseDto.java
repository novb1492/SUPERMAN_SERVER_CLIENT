package com.kimcompany.jangbogbackendver2.Util.Dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ResponseDto {
    private String message;
    private Object data;
    private int state;
}

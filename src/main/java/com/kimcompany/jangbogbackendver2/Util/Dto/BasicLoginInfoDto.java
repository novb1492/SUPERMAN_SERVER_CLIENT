package com.kimcompany.jangbogbackendver2.Util.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BasicLoginInfoDto {
    private String id;
    private String role;

    public static BasicLoginInfoDto set(String id,String role){
        BasicLoginInfoDto basicLoginInfoDto = new BasicLoginInfoDto();
        basicLoginInfoDto.setId(id);
        basicLoginInfoDto.setRole(role);
        return basicLoginInfoDto;
    }
}

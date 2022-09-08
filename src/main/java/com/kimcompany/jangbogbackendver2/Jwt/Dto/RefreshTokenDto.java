package com.kimcompany.jangbogbackendver2.Jwt.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class RefreshTokenDto {
    private String accessToken;
    private String refreshToken;
    private int count;
    private Timestamp created;
    private Timestamp update;
    private long id;

    public static RefreshTokenDto setFirst(String accessToken,String refreshToken,long id){
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setRefreshToken(refreshToken);
        refreshTokenDto.setAccessToken(accessToken);
        refreshTokenDto.setCount(0);
        refreshTokenDto.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        refreshTokenDto.setId(id);
        return refreshTokenDto;
    }
}

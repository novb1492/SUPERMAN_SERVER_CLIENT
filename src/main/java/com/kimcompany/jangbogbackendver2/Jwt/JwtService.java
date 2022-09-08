package com.kimcompany.jangbogbackendver2.Jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class JwtService {

    public String getAccessToken(long id) {
        return JWT.create().withSubject(PropertiesText.accessTokenName).withClaim("id",id).withExpiresAt(new Date(System.currentTimeMillis()+1000* PropertiesText.accessTokenExpireMin)).sign(Algorithm.HMAC512(PropertiesText.accessTokenSign));
    }
    public String getToken(String tokenName,long expireSecond,String jwtSign) {
        return JWT.create().withSubject(tokenName).withExpiresAt(new Date(System.currentTimeMillis()+1000*expireSecond)).sign(Algorithm.HMAC512(jwtSign));
    }
    public long openAccessToken(String accessToken) {
        return JWT.require(Algorithm.HMAC512(PropertiesText.accessTokenSign)).build().verify(accessToken).getClaim("id").asLong();
    }
    public void openEmptyJwt(String token,String sign){
        JWT.require(Algorithm.HMAC512(sign)).build().verify(token);
    }

}

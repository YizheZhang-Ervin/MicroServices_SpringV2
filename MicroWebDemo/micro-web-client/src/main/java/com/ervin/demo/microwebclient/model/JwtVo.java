package com.ervin.demo.microwebclient.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class JwtVo {
    String userId;
    String password;
    String token;
    String refreshToken;
    String status;
    Date tokenExpireTime;
    Date refreshTokenExpireTime;
}

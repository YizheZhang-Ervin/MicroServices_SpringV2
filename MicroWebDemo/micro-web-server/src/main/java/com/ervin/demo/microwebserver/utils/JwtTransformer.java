package com.ervin.demo.microwebserver.utils;

import com.ervin.demo.microwebserver.entity.Jwt;
import com.ervin.demo.microwebserver.model.JwtVo;

public class JwtTransformer {

    public static Jwt JwtVo2Jwt(JwtVo oldData,Long id,String pwd){
        Jwt newData = new Jwt();
        newData.setId(id)
                .setUserId(oldData.getUserId())
                .setEncryptedPwd(pwd)
                .setToken(oldData.getToken())
                .setRefreshToken(oldData.getRefreshToken())
                .setTokenExpireTime(oldData.getTokenExpireTime())
                .setRefreshTokenExpireTime(oldData.getRefreshTokenExpireTime());
        return newData;
    }

    public static JwtVo Jwt2JwtVo(Jwt oldData,String status,boolean hiddenToken,boolean hiddenPwdTime){
        JwtVo newData = new JwtVo();
        newData.setUserId(oldData.getUserId());
        newData.setStatus(status);
        if(!hiddenToken){
            newData.setToken(oldData.getToken());
            newData.setRefreshToken(oldData.getRefreshToken());
        }
        if(!hiddenPwdTime){
            newData.setPassword(oldData.getEncryptedPwd());
            newData.setTokenExpireTime(oldData.getTokenExpireTime());
            newData.setRefreshTokenExpireTime(oldData.getRefreshTokenExpireTime());
        }
        return newData;
    }

    public static JwtVo JwtVo2JwtVo(JwtVo oldData,String status,boolean hiddenToken,boolean hiddenPwdTime){
        JwtVo newData = new JwtVo();
        newData.setUserId(oldData.getUserId());
        newData.setStatus(status);
        if(!hiddenToken){
            newData.setToken(oldData.getToken());
            newData.setRefreshToken(oldData.getRefreshToken());
        }
        if(!hiddenPwdTime){
            newData.setPassword(oldData.getPassword());
            newData.setTokenExpireTime(oldData.getTokenExpireTime());
            newData.setRefreshTokenExpireTime(oldData.getRefreshTokenExpireTime());
        }
        return newData;
    }
}

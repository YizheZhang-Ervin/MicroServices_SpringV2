package com.ervin.demo.microwebserver.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ervin.demo.microwebserver.entity.Jwt;
import com.ervin.demo.microwebserver.mapper.JwtMapper;
import com.ervin.demo.microwebserver.model.JwtVo;
import com.ervin.demo.microwebserver.utils.JwtTransformer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtServiceImpl extends ServiceImpl<JwtMapper,Jwt> implements JwtService  {
    @Value("${jwt.secret:abc123}")
    String SECRET;

    //token签发者
    @Value("${jwt.issuer:shsyq}")
    String ISSUER;

    //token过期时间
    @Value("${jwt.expire}")
    Long EXPIRE_DATE = 1000 * 1800L;

    //refreshToken过期时间
    @Value("${jwt.refreshExpire}")
    Long REFRESH_EXPIRE_DATE = 2 * 1000 * 1800L;

    @Resource
    JwtMapper jwtMapper;

    // 登录
    public JwtVo login(JwtVo reqVo){
        JwtVo responseVo = new JwtVo();
        // 根据userId查用户
        QueryWrapper<Jwt> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","userId","encryptedPwd").eq("userId",reqVo.getUserId());
        Jwt jwt = jwtMapper.selectOne(queryWrapper);
        // 密码加密后比对
        String encryptedPwd = DigestUtils.md5DigestAsHex(reqVo.getPassword().getBytes());
        if(!encryptedPwd.equals(jwt.getEncryptedPwd())){
            return responseVo.setStatus("failed,userId or password incorrect");
        }
        // 生成token、refreshToken、当前时间、过期时间,更新库
        JwtVo tokenVo = createToken(reqVo);
        String token = tokenVo.getToken();
        jwt.setToken(token)
                .setRefreshToken(tokenVo.getRefreshToken())
                .setTokenExpireTime(tokenVo.getTokenExpireTime())
                .setRefreshTokenExpireTime(tokenVo.getRefreshTokenExpireTime());
        int res = jwtMapper.updateById(jwt);
        // 返回响应
        if (res > 0) {
            responseVo = JwtTransformer.Jwt2JwtVo(jwt,"success",false,true);
        } else {
            responseVo.setStatus("failed, DB error");
        }
        return responseVo;
    }

    // 验证token
    public JwtVo verifyToken(String token){
        JwtVo responseVo = new JwtVo();
        // token为空直接返回
        if(StringUtils.isEmpty(token)){
            return responseVo.setStatus("failed, empty token");
        }
        // 解析token
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
        }catch (Exception e){
            return responseVo.setStatus(e.getMessage());
        }
        // 判断token是否能解析出
        if(ObjectUtils.isEmpty(decodedJWT)){
            return responseVo.setStatus("failed, error token");
        }
        String userId = decodedJWT.getClaim("userId").asString();
        Date expire = decodedJWT.getExpiresAt();
        Date nowTime = new Date();
        // 判断用户名能解析出来
        boolean flagUserId = !StringUtils.isEmpty(userId);
        // token是否有效
        boolean flagTokenFront = nowTime.before(expire);
        // 二重过期验证：后端时间戳（例如token里的过期时间被篡改的情况）
        QueryWrapper<Jwt> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("userId","tokenExpireTime")
                .eq("userId",userId)
                .gt("tokenExpireTime",nowTime);
        Jwt res = jwtMapper.selectOne(queryWrapper);
        boolean flagTokenBack = !ObjectUtils.isEmpty(res);
        if(flagUserId && flagTokenFront && flagTokenBack){
            return responseVo.setStatus("success").setUserId(userId);
        }else{
            return responseVo.setStatus("failed, expired token");
        }
    }

    // 更新续签
    public JwtVo renewToken(JwtVo jwtVo){
        JwtVo responseVo = new JwtVo();
        // 判断refreshtoken是否有效期内
        String refreshToken = jwtVo.getRefreshToken();
        QueryWrapper<Jwt> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("refreshToken",refreshToken).gt("refreshTokenExpireTime",new Date());
        Jwt jwt = jwtMapper.selectOne(queryWrapper);
        // 失效返回需要重登的信息
        if(ObjectUtils.isEmpty(jwt)){
            return responseVo.setStatus("failed, relogin");
        }else{
            // 不失效，重新生成token、refreshToken、当前时间、过期时间,更新库
            JwtVo data = JwtTransformer.Jwt2JwtVo(jwt,null,true,true);
            JwtVo renewData = createToken(data);
            Long id = jwt.getId();
            String pwd = jwt.getEncryptedPwd();
            Jwt renewData2 = JwtTransformer.JwtVo2Jwt(renewData,id,pwd);
            int res = jwtMapper.updateById(renewData2);
            // 返回响应
            if (res > 0) {
                return JwtTransformer.JwtVo2JwtVo(renewData,"success",false,true);
            } else {
                return responseVo.setStatus("failed, DB error");
            }
        }
    }

    // 注册
    public JwtVo register(JwtVo jwtVo){
        JwtVo responseVo = new JwtVo();
        responseVo.setUserId(jwtVo.getUserId());
        // 密码加密
        String encryptedPwd = DigestUtils.md5DigestAsHex(jwtVo.getPassword().getBytes());
        // 存库
        Jwt jwt = new Jwt().setUserId(jwtVo.getUserId()).setEncryptedPwd(encryptedPwd);
        int res = jwtMapper.insert(jwt);
        if (res > 0) {
            responseVo.setStatus("success");
        } else {
            responseVo.setStatus("failed, DB error");
        }
        return responseVo;
    }

    // 注销
    public JwtVo deregister(JwtVo jwtVo){
        JwtVo responseVo = new JwtVo();
        String userId = jwtVo.getUserId();
        responseVo.setUserId(userId);
        QueryWrapper<Jwt> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        int res = jwtMapper.delete(queryWrapper);
        if (res > 0) {
            responseVo.setStatus("success");
        } else {
            responseVo.setStatus("failed, DB error");
        }
        return responseVo;
    }

    // 登出
    public JwtVo logout(JwtVo jwtVo){
        JwtVo responseVo = new JwtVo();
        String userId = jwtVo.getUserId();
        String token = jwtVo.getToken();
        responseVo.setUserId(userId);
        // 查库
        UpdateWrapper<Jwt> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("token",null);
        updateWrapper.set("refreshToken",null);
        updateWrapper.set("tokenExpireTime",null);
        updateWrapper.set("refreshTokenExpireTime",null);
        updateWrapper.eq("userId",userId);
        updateWrapper.eq("token",token);
        int res = jwtMapper.update(null,updateWrapper);
        if (res > 0) {
            responseVo.setStatus("success");
        } else {
            responseVo.setStatus("failed, DB error");
        }
        // token手动过期
        return responseVo;
    }

    // 创建token
    public JwtVo createToken(JwtVo reqVo){
        // reqVo: userId,password
        // resVo：userId,token,refreshToken,tokenExpireTime,refreshTokenExpireTime,Status
        JwtVo responseVo = new JwtVo();
        // 当前时间
        Date now = new Date();
        Date tokenExpireTime = new Date(now.getTime() + EXPIRE_DATE);
        Date refreshTokenExpireTime = new Date(now.getTime() + REFRESH_EXPIRE_DATE);
        // 算法
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        // 用户id
        String userId = reqVo.getUserId();
        // token
        String token = JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(now)
                .withExpiresAt(tokenExpireTime)
                .withClaim("userId", userId)
                .sign(algorithm);
        // 用于续签的token
        String refreshToken = UUID.randomUUID().toString();
        // 返回响应
        responseVo.setUserId(userId)
                .setToken(token)
                .setRefreshToken(refreshToken)
                .setTokenExpireTime(tokenExpireTime)
                .setRefreshTokenExpireTime(refreshTokenExpireTime)
                .setStatus("success");
        return responseVo;
    }
}

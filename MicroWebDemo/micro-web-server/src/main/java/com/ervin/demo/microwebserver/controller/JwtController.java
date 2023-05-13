package com.ervin.demo.microwebserver.controller;

import com.ervin.demo.microwebserver.model.JwtVo;
import com.ervin.demo.microwebserver.service.JwtServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/jwt")
public class JwtController {

    @Resource
    JwtServiceImpl jwtService;

    @PostMapping("login")
    public JwtVo login(@RequestBody JwtVo reqVo, HttpServletResponse response){
        // reqVo: userId,password
        // resVo: userId,token,refreshToken,status
        JwtVo resVo = jwtService.login(reqVo);
        // token放在请求头
        response.setHeader("authorization", resVo.getToken());
        return resVo;
    }

    @RequestMapping(value = "verify",method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    public JwtVo verifyToken(HttpServletRequest request, HttpServletResponse response){
        // header: Authentication
        // resVo: UserId,Status
        String token = request.getHeader("Authentication");
        JwtVo resVo = jwtService.verifyToken(token);
        response.setHeader("Authentication",token);
        return resVo;
    }

    @PostMapping("renew")
    public JwtVo renewToken(@RequestBody JwtVo reqVo, HttpServletResponse response){
        // reqVo：userId,refreshToken
        // resVo：userId,token,refreshToken,status
        JwtVo resVo = jwtService.renewToken(reqVo);
        // 新token放在请求头
        response.setHeader("authorization", resVo.getToken());
        return resVo;
    }

    @PostMapping("register")
    public JwtVo register(@RequestBody JwtVo reqVo){
        // reqVo：userId,password
        // resVo：userId,status
        JwtVo resVo = jwtService.register(reqVo);
        return resVo;
    }

    @PostMapping("deregister")
    public JwtVo deregister(@RequestBody JwtVo reqVo,HttpServletRequest request){
        // header: Authentication
        // reqVo：userId
        // resVo：userId,status
        String token = request.getHeader("Authentication");
        reqVo.setToken(token);
        // 没有token也可以注销用户
        JwtVo resVo = jwtService.deregister(reqVo);
        return resVo;
    }

    @PostMapping("logout")
    public JwtVo logout(@RequestBody JwtVo reqVo,HttpServletRequest request)  {
        // header: Authentication
        // reqVo：userId
        // resVo：userId,Status
        String token = request.getHeader("Authentication");
        reqVo.setToken(token);
        JwtVo resVo = jwtService.logout(reqVo);
        return resVo;
    }
}
package com.ervin.demo.microwebserver.interceptors;

import com.ervin.demo.microwebserver.model.JwtVo;
import com.ervin.demo.microwebserver.service.JwtServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    JwtServiceImpl jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        // 获取参数
        String token = request.getHeader("Authentication");
        if(StringUtils.isEmpty(token)) return failProcess(response);
        // 解析token
        JwtVo jwtVo = jwtService.verifyToken(token);
        request.setAttribute("userId", jwtVo.getUserId());
        if(jwtVo.getStatus().equalsIgnoreCase("expired")){
            return failProcess(response);
        }else{
            return true;
        }
    }

    public boolean failProcess(HttpServletResponse response){
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(401);
        return false;
    }
}

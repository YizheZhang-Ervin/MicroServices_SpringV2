package com.ervin.demo.microwebclient.annotation;


import com.alibaba.fastjson2.JSON;
import com.ervin.demo.microwebclient.component.GoHttp;
import com.ervin.demo.microwebclient.model.JwtVo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Aspect
@Component
@Slf4j
public class JwtAuthAop {

    @Resource
    GoHttp goHttp;
    @Value("${microweb.serverName}")
    String serverIp;
    @Value("${microweb.serverPort}")
    String serverPort;

    @Pointcut("@annotation(com.ervin.demo.microwebclient.annotation.JwtAuthAnnotation)")
    private void pointcut(){
    }

    @Before("pointcut() && @annotation(jwtAuthAnnotation)")
    public void beforeHandler(JwtAuthAnnotation jwtAuthAnnotation) throws Exception{
        String val = jwtAuthAnnotation.value();
        log.info("===="+val);
        try {
            ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            assert sra != null;
            HttpServletRequest request = sra.getRequest();
            String token = request.getHeader("Authentication");
            String url = String.format("http://%s:%s/jwt/verify", serverIp, serverPort);
            String resStr = goHttp.post(url, "", token);
            JwtVo resVo = JSON.parseObject(resStr, JwtVo.class);
            // log.info(String.valueOf(resVo));
            if(!resVo.getStatus().equalsIgnoreCase("success")){
                throw new Exception(resVo.getStatus());
            }
        }catch (IOException e){
            log.warn(e.getMessage());
        }
    }
}
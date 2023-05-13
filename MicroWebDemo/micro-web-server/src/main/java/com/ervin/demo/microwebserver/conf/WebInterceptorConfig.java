package com.ervin.demo.microwebserver.conf;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.ervin.demo.microwebserver.interceptors.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebInterceptorConfig extends WebMvcConfigurationSupport {

    @Resource
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 过滤免检路由
        List<String> excludePath = new ArrayList<>();
        String jwtApi = "/jwt/*";
        String mainApi = "/";
        String eurekaApi = "/eureka/**";
        String homeApi = "/home/*";
        excludePath.add(jwtApi);
        excludePath.add(mainApi);
        excludePath.add(eurekaApi);
        excludePath.add(homeApi);
        registry.addInterceptor(jwtInterceptor).excludePathPatterns(excludePath);
    }

    // 解决重写WebMvcConfigurationSupport之后eureka资源找不到的问题
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/eureka/**").addResourceLocations("classpath:/static/eureka/");
    }

    // 解决引入eureka以后接口都默认返回xml的问题
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonConfig.setCharset(Charset.forName("GB2312"));//解决中文系列化后成乱码
        fastJsonConverter.setFastJsonConfig(fastJsonConfig);

        List<MediaType> mediaTypes = new ArrayList<>(16);
        mediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        mediaTypes.add(MediaType.APPLICATION_CBOR);
        mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        fastJsonConverter.setSupportedMediaTypes(mediaTypes);

        //converters.add(fastJsonConverter);//这会让fastJsonConverter排在消息转换器管道列表的最后，可能会轮不到它处理消息转换
        converters.add(0, fastJsonConverter);//要显示指明将fastJsonConverter排在消息转换器管道列表的首位
    }
}
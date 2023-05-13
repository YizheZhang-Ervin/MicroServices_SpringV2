package com.ervin.demo.microwebclient.component;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class GoHttp {
    @Resource
    OkHttpClient okHttpClient;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public String get(String url, String token){
        Request request = new Request.Builder().header("Authentication",token)
                .url(url).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return response.body().string();
        }catch (Exception e){
            log.warn(e.getMessage());
        }
        return null;
    }

    public String post(String url, String json,String token){
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().header("Authentication",token)
                .url(url).post(body).build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            return response.body().string();
        }catch (Exception e){
            log.warn(e.getMessage());
        }
        return null;
    }
}

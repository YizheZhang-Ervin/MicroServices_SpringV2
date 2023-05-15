package com.ervin.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// 防止ws启动报错
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebsocketDemoApplicationTests {

    @Test
    void contextLoads() {
    }

}

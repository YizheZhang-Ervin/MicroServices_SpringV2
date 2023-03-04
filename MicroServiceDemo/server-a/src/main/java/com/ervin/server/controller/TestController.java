package com.ervin.server.controller;

import com.ervin.server.service.FolderCheckService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    FolderCheckService folderCheckService;

    // 判断输入的url是否为空
    @GetMapping("/judgeEmpty")
    public String test1(@RequestParam String urls){
        List<String> urlLis = Arrays.asList(urls.split(","));
        System.out.println(urlLis);
        System.out.println(urlLis.isEmpty());
        System.out.println(urlLis.size()==0);
        System.out.println(CollectionUtils.isEmpty(urlLis));
        System.out.println(ObjectUtils.isEmpty(urlLis));
        return urlLis.get(0);
    }

    // 加密检查
    @GetMapping("/encode")
    public String test2(@RequestParam(value = "plaintext") String plaintext,
                         @RequestParam(value="encodetext",required = false) String encodetext,
                         @RequestParam(value = "type",defaultValue = "encrypt") String type) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if("encrypt".equalsIgnoreCase(type)){
            return "Encrypt Result: " + encoder.encode(plaintext);
        }else{
            return "Check Result: " + encoder.matches(plaintext,encodetext);
        }
    }

    // 判断值是否一致
    @GetMapping("/booleanCheck")
    public String test3() {
        Boolean flag = Boolean.TRUE;
        boolean result1 = flag.equals(true);
        int result2 = flag.compareTo(true);
        boolean result3 = flag.booleanValue()==true;
        return result1+","+result2+","+result3;
    }

    // 检查两个目录(绝对路径)文件内容是否一致
    @GetMapping("/checkFolder")
    public String test4(@RequestParam(value = "path1") String path1,
                        @RequestParam(value = "format",defaultValue = "MD5") String format,
                        @RequestParam(value = "type",defaultValue = "generate") String type){
        // 比较文件长度、权限、MD5/SHA1
        return folderCheckService.checkFolder(path1, format,type);
    }

    // 逐字节对比文件
    @GetMapping("/compareFile")
    public boolean test5(@RequestParam(value = "path1") String path1,
                        @RequestParam(value = "path2") String path2){
        return folderCheckService.checkFileByBytes(path1,path2);
    }

    // 建文件
    @GetMapping("/createFile")
    public boolean test6(@RequestParam(value="path") String path,
                         @RequestParam(value="power",defaultValue = "775") String power){
        return folderCheckService.createFile(path,power);
    }
}
